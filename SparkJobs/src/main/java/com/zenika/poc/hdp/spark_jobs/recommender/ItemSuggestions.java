package com.zenika.poc.hdp.spark_jobs.recommender;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

import java.util.Arrays;

public class ItemSuggestions {

    /**
     * Main function that retrieve recommendations for a given user using a given model
     *
     * Aguments:
     *  - model path
     *  - user id
     *  - number of suggestions
     *  - recommendations path
     *
     * @param args
     * @throws Exception
     *
     * sudo spark-submit --class com.zenika.poc.hdp.spark_jobs.recommender.ItemSuggestion poc.hdp-1.0-SNAPSHOT.jar "hdfs://sandbox.hortonworks.com:8020/poc/model1m" "1" "10" "hdfs://sandbox.hortonworks.com:8020/poc/recommendations1m"
     */
    public static void main(String... args) throws Exception {
        if (args.length != 4) {
            throw new Exception("Expecting three arguments");
        }

        // Reading arguments: the model path, the user id, the number of suggestions, the recommendations destination path
        String modelPath = args[0];
        int user = Integer.parseInt(args[1]);
        int nbMovies = Integer.parseInt(args[2]);
        String recommendationsPath = args[3];

        // Context initialization
        SparkConf sparkConf = new SparkConf().setAppName("ALS item suggestions");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // Loading previously computed model
        MatrixFactorizationModel model = MatrixFactorizationModel.load(jsc.sc(), modelPath);
        // Computing recommendations for the given user id
        Rating[] recommendations = model.recommendProducts(user, nbMovies);

        // Saving recommendations result
        jsc.parallelize(Arrays.asList(recommendations)).saveAsTextFile(recommendationsPath);

        // Stopping context
        jsc.stop();
    }
}
