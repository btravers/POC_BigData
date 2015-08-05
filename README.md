# POC BigData

## Introduction

This application illustrates the use of collaborative filtering for movie recommendations. We will use rating data sets from MovieLens web site [http://grouplens.org/datasets/movielens/](http://grouplens.org/datasets/movielens/).

## Building

First, set up the Elasticsearch cluster:

	docker run -d -p 9200:9200 -p 9300:9300 -v "$PwD/elasticsearch":/usr/share/elasticsearch/data --name movie_library_es elasticsearch:1.7
	chmod +x es_index_creation.sh
	./es_index_creation.sh localhost:9200

Second, set up the backend:

	make
	docker run -d -p 8080:8080 --link movie_library_es:es.url --name movie_library_api btravers/movie_library_api:1.0.0

Finaly, set up frontend:

	make
	docker run -d -p 80:80/tcp --link movie_library_api:movie_library_api --name movie_library btravers/movie_library:1.0.0

## Computing ALS model using Spark and MLlib

## Feeding your Elasticearch cluster with data
	

