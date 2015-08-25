# POC BigData

## Introduction

This application illustrates the use of collaborative filtering for movie recommendations. We will use rating data sets from MovieLens web site ([http://grouplens.org/datasets/movielens/](http://grouplens.org/datasets/movielens/)).

Requirements:
- Maven
- Docker
- Node.js / npm
- Bower
- Gem sass
- Gulp

## Building

Before building the project, clone the repository:

	git clone https://github.com/btravers/POC_BigData.git

First, set up the Elasticsearch cluster:

	cd POC_BigData/elasticsearch
	docker run -d -v "$PWD"/data/:/usr/share/elasticsearch/data --name movie_library_es elasticsearch:1.7
	
Once the Elasticsearch cluster is running, we need to create the index where data will be stored. You can retrieve Elasticsearch container IP address using the following command:

	docker inspect movie_library_es | grep IPAddress
	
Then, create the index using:

	./es_index_creation.sh ES_CONTAINER_IP:9200

Set up the backend:

	cd ../backend
	make
	docker run -d --link movie_library_es:es.url --name movie_library_api btravers/movie_library_api:1.0.0

Finally, set up frontend:

	cd ../frontend
	make
	docker run -d -p 80:80 --link movie_library_api:movie_library_api --name movie_library btravers/movie_library:1.0.0

## Computing ALS model using Spark and MLlib

Package SparkJobs project using maven:

	cd ../spark
	mvn package
	
Download data from MovieLens:

	wget files.grouplens.org/datasets/movielens/ml-1m.zip
	unzip ml-1m.zip

Once the artifact built, we set up Spark in a container:

	docker build -t btravers/spark:0.1.0
	docker pull kiwenlau/hadoop-slave:0.1.0
	./start-up.sh
	
When the script ends, you are running bash shell in master container. Compute ALS model:
	
	$SPARK_HOME/bin/spark-submit --class com.zenika.poc.hdp.spark_jobs.recommender.ModelComputation /spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar file:/data/ratings.dat :: file:/data/model1m > /data/res.log			

## Feeding your Elasticsearch cluster with data

Using master container bash shell, perform the following commands:

	$SPARK_HOME/bin/spark-submit --class com.zenika.poc.hdp.spark_jobs.es_feeder.Feeder /spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar file:/data/movies.dat file:/data/ratings.dat ES_CONTAINER_IP:9200
	$SPARK_HOME/bin/spark-submit --class com.zenika.poc.hdp.spark_jobs.es_feeder.Recommendations /spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar file:/data/model1m NB_USERS NB_RECOMMENDATIONS ES_CONTAINER_IP:9200


## Spark on YARN (optional)

We can use YARN in order to manage Spark jobs.

Start Hadoop:

	./start-hadoop.sh

Put files into HDFS:

	hadoop dfs -mkdir /data
	hadoop dfs -put /data/ratings.dat /data/ratings.dat
	hadoop dfs -put /data/movies.dat /data/movies.dat
	
Run Spark jobs on YARN:

	$SPARK_HOME/bin/spark-submit --master yarn-cluster --class com.zenika.poc.hdp.spark_jobs.recommender.ModelComputation /spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar /data/ratings.dat :: /data/model1m
	$SPARK_HOME/bin/spark-submit --master yarn-cluster --class com.zenika.poc.hdp.spark_jobs.es_feeder.Feeder /spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar /data/movies.dat /data/ratings.dat ES_CONTAINER_IP:9200
	$SPARK_HOME/bin/spark-submit --master yarn-cluster --class com.zenika.poc.hdp.spark_jobs.es_feeder.Recommendations /spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar /data/model1m NB_USERS NB_RECOMMENDATIONS ES_CONTAINER_IP:9200


	

