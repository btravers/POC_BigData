# POC BigData

## Introduction

This application illustrates the use of collaborative filtering for movie recommendations. We will use rating data sets from MovieLens web site [http://grouplens.org/datasets/movielens/](http://grouplens.org/datasets/movielens/). 

Requirements:
- Maven
- Docker
- Spark

## Building

First, set up the Elasticsearch cluster:

	docker run -d -p 9200:9200 -p 9300:9300 -v "$PWD":/usr/share/elasticsearch/data --name movie_library_es elasticsearch:1.7
	chmod +x es_index_creation.sh
	./es_index_creation.sh localhost:9200

Second, set up the backend:

	make
	docker run -d -p 8080:8080 --link movie_library_es:es.url --name movie_library_api btravers/movie_library_api:1.0.0

Finaly, set up frontend:

	make
	docker run -d -p 80:80/tcp --link movie_library_api:movie_library_api --name movie_library btravers/movie_library:1.0.0

## Computing ALS model using Spark and MLlib

Package SparkJobs project using maven:

	mvn package

It should produce an artifact with dependencies in the target directory. Use this artifact with Spark in order to compute the ALS model.

	spark-submit --class com.zenika.poc.hdp.spark_jobs.recommender.ModelComputation spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar "PATH_TO/ratings.dat" "::" "RESULTING_MODEL_PATH"

## Feeding your Elasticearch cluster with data

Now, we need to feed our Elasticsearch with data. You can retrieve Elasticsearch container IP address using the following command:

	docker inspect movie_library_es | grep IPAddress

Then, using the artifact from the previous part, perform the two following commands.

	spark-submit --class com.zenika.poc.hdp.spark_jobs.es.feeder.Feeder spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar "PATH_TO/movies.dat" "PATH_TO/ratings.dat" "ES_CONTAINER_IP:9200"

	spark-submit --class com.zenika.poc.hdp.spark_jobs.es.feeder.Recommendations spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar "RESULTING_MODEL_PATH" "NB_USER" "NB_RECOMMENDATIONS" "ES_CONTAINER_IP:9200"


	

