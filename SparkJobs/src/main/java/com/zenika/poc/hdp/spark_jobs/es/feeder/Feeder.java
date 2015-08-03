package com.zenika.poc.hdp.spark_jobs.es.feeder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import scala.Tuple2;

import java.util.*;

public class Feeder {

    /**
     *
     * @param args
     * @throws Exception
     *
     * sudo spark-submit --class com.zenika.poc.hdp.spark_jobs.es.feeder.Feeder spark_jobs-1.0-SNAPSHOT.jar hdfs://sandbox.hortonworks.com:8020/poc/data/movielens_1m/movies.dat hdfs://sandbox.hortonworks.com:8020/poc/data/movielens_1m/ratings.dat 172.17.0.2:9200
     */
    public static void main(String... args) throws Exception {
        if (args.length != 3) {
            throw new Exception("Expecting 3 arguments");
        }

        String movieFile = args[0];
        String ratingFile = args[1];
        String es = args[2];

        // Initializing context
        SparkConf sparkConf = new SparkConf().setAppName("Feeder");
        sparkConf.set("es.index.auto.create", "true");
        sparkConf.set("es.nodes", es);
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // Parsing movies
        JavaRDD<Map<String, Object>> movies = jsc.textFile(movieFile).map(new Function<String, String[]>() {
            @Override
            public String[] call(String s) throws Exception {
                return s.split("::");
            }
        }).map(new Function<String[], Map<String, Object>>() {
            @Override
            public Map<String, Object> call(String[] strings) throws Exception {
                Map<String, Object> movie = new HashMap<>();

                movie.put("id", strings[0]);
                movie.put("title", strings[1]);
                movie.put("genres", Arrays.asList(strings[2].split("\\|")));

                return movie;
            }
        });

        // Parsing ratings
        JavaRDD<Map<String, Object>> ratings = jsc.textFile(ratingFile).map(new Function<String, String[]>() {
            @Override
            public String[] call(String s) throws Exception {
                return s.split("::");
            }
        }).map(new Function<String[], Map<String, Object>>() {
            @Override
            public Map<String, Object> call(String[] strings) throws Exception {
                Map<String, Object> rating = new HashMap<>();

                rating.put("user", strings[0]);
                rating.put("movie", strings[1]);
                rating.put("mark", strings[2]);

                return rating;
            }
        });

        // Computing mean mark and nb votes per movie
        JavaPairRDD<String, Tuple2<Double, Integer>> movieMeanNb = JavaPairRDD.fromJavaRDD(ratings.map(new Function<Map<String, Object>, Tuple2<String, Map<String, Object>>>() {
            @Override
            public Tuple2<String, Map<String, Object>> call(Map<String, Object> stringObjectMap) throws Exception {
                Tuple2<String, Map<String, Object>> tuple = new Tuple2<>(stringObjectMap.get("movie").toString(), stringObjectMap);
                return tuple;
            }
        })).groupByKey().mapValues(new Function<Iterable<Map<String, Object>>, Tuple2<Double, Integer>>() {
            @Override
            public Tuple2<Double, Integer> call(Iterable<Map<String, Object>> maps) throws Exception {
                double mean = 0.0;
                int length = 0;
                for (Map<String, Object> rating : maps) {
                    mean += Integer.parseInt(rating.get("mark").toString());
                    length++;
                }
                return new Tuple2<>(mean / length, length);
            }
        });

        // Merging mean and nb votes with previous movie information
        JavaRDD<Map<String, Object>> moviesWithMark = JavaPairRDD.fromJavaRDD(movies.map(new Function<Map<String, Object>, Tuple2<String, Map<String, Object>>>() {
            @Override
            public Tuple2<String, Map<String, Object>> call(Map<String, Object> stringObjectMap) throws Exception {
                return new Tuple2<>(stringObjectMap.get("id").toString(), stringObjectMap);
            }
        })).join(movieMeanNb).map(new Function<Tuple2<String,Tuple2<Map<String,Object>,Tuple2<Double,Integer>>>, Map<String, Object>>() {
            @Override
            public Map<String, Object> call(Tuple2<String, Tuple2<Map<String, Object>, Tuple2<Double, Integer>>> stringTuple2Tuple2) throws Exception {
                Map<String, Object> res = stringTuple2Tuple2._2()._1();
                res.put("mark", stringTuple2Tuple2._2()._2()._1());
                res.put("nb", stringTuple2Tuple2._2()._2()._2());
                return res;
            }
        });

        // Writing results
        JavaEsSpark.saveToEs(moviesWithMark, "library/movie");
        JavaEsSpark.saveToEs(ratings, "library/rating");
    }
}
