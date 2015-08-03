package com.zenika.poc.hdp.spark_jobs.recommender;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import scala.Tuple2;

public class Utils {

    /**
     * Function that compute model's mean square error over a given data set.
     * @param model Model to evaluate
     * @param dataset Data set
     * @return Mean square error
     */
    static double computeMSE(MatrixFactorizationModel model, JavaRDD<Rating> dataset) {
        // Generating RDD of couples (userId, movieId)
        JavaRDD<Tuple2<Object, Object>> predictionSet = dataset.map(
                new Function<Rating, Tuple2<Object, Object>>() {
                    @Override
                    public Tuple2<Object, Object> call(Rating rating) throws Exception {
                        return new Tuple2<>((Object) rating.user(), (Object) rating.product());
                    }
                }
        );

        // Performing prediction on previously generated RDD
        JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(
                model.predict(
                        predictionSet.rdd()
                ).toJavaRDD().map(
                        new Function<Rating, Tuple2<Tuple2<Integer, Integer>, Double>>() {
                            @Override
                            public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating rating) throws Exception {
                                return new Tuple2<>(new Tuple2<>(rating.user(), rating.product()), rating.rating());
                            }
                        }
                )
        );

        // Generating RDD of couples (prediction, rating)
        JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD.fromJavaRDD(
                dataset.map(
                        new Function<Rating, Tuple2<Tuple2<Integer, Integer>, Double>>() {
                            @Override
                            public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating rating) throws Exception {
                                return new Tuple2<>(new Tuple2<>(rating.user(), rating.product()), rating.rating());
                            }
                        }
                )
        ).join(predictions).values();

        // Returning  MSE
        return JavaDoubleRDD.fromRDD(
                ratesAndPreds.map(
                        new Function<Tuple2<Double, Double>, Object>() {
                            @Override
                            public Object call(Tuple2<Double, Double> doubleDoubleTuple2) throws Exception {
                                return Math.pow(doubleDoubleTuple2._1() - doubleDoubleTuple2._2(), 2);
                            }
                        }
                ).rdd()
        ).mean();
    }

    /**
     * Function that generate a RDD of ratings from raw data
     * @param data Raw data
     * @param separator The separator
     * @return RDD of rating
     */
    static JavaRDD<Rating> generateRatings(JavaRDD<String> data, final String separator) {
        return data.map(
                // Splitting lines using the specified separator
                new Function<String, String[]>() {
                    @Override
                    public String[] call(String s) throws Exception {
                        return s.split(separator);
                    }
                }
        ).filter(
                // Removing lines which are not complete
                new Function<String[], Boolean>() {
                    @Override
                    public Boolean call(String[] strings) throws Exception {
                        return strings.length == 4;
                    }
                }
        ).map(
                // Creating rating from line information
                new Function<String[], Rating>() {
                    @Override
                    public Rating call(String[] strings) throws Exception {
                        return new Rating(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Float.parseFloat(strings[2]));
                    }
                }
        );
    }

}
