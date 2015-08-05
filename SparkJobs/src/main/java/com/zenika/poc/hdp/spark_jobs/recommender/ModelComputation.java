package com.zenika.poc.hdp.spark_jobs.recommender;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

public class ModelComputation {

    /**
     * Main function that compute best ALS model for a given data set and save resulting model on HDP.
     *
     * Aguments:
     *  - data path
     *  - separator
     *  - model path
     *
     * @param args
     * @throws Exception
     *
     * "hdfs://sandbox.hortonworks.com:8020/poc/data/movielens_100k/data.tsv" with "\t" separator
     * "hdfs://sandbox.hortonworks.com:8020/poc/data/movielens_1m/ratings.dat" with "::" separator
     * "hdfs://sandbox.hortonworks.com:8020/poc/data/movielens_10m/ratings.dat" with "::" separator
     * "hdfs://sandbox.hortonworks.com:8020/poc/data/movielens_20m/ratings.csv" with "," separator
     *
     * spark-submit --class com.zenika.poc.hdp.spark_jobs.recommender.ModelComputation spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar "hdfs://sandbox.hortonworks.com:8020/poc/data/movielens_1m/ratings.dat" "::" "hdfs://sandbox.hortonworks.com:8020/poc/model1m" > recommender1m_result.txt
     */
    public static void main(String... args) throws Exception {
        if (args.length != 3) {
            throw new Exception("Expecting three arguments");
        }

        // Reading arguments: the data file path and the data separator
        String dataPath = args[0];
        final String separator = args[1];
        String modelPath = args[2];

        // Weights used to generate data subsets
        double[] weights = {.6, .2, .2};
        // Number of iteration performed during ALS
        int numIterations = 20;

        // Context initialization
        SparkConf sparkConf = new SparkConf().setAppName("ALS model computation");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // Transforming file content into JavaRDD
        JavaRDD<String> data = jsc.textFile(dataPath);

        // Transforming each lines into ratings
        JavaRDD<Rating> ratings = Utils.generateRatings(data, separator);

        // Generating train set, cross validation set and test set
        JavaRDD<Rating>[] sets = ratings.randomSplit(weights);

        // Caching generated subsets
        sets[0].cache();
        sets[1].cache();
        sets[2].cache();

        MatrixFactorizationModel bestModel = null;
        double bestMSE = 0;

        // Computing several model with different values for rank
        for (int rank = 1; rank <= 10; rank++) {
            // Training model with training set
            MatrixFactorizationModel model = ALS.train(sets[0].rdd(), rank, numIterations, 0.01);

            // Computing MSE over cross validation set
            double MSE = Utils.computeMSE(model, sets[1]);
            System.out.println("MSE for rank " + rank + ": " + MSE);

            // Saving model if better than the old one
            if (bestModel == null || MSE < bestMSE) {
                bestModel = model;
                bestMSE = MSE;
            }
        }

        // Evaluating best model on test set
        double MSE = Utils.computeMSE(bestModel, sets[2]);

        System.out.println("Model rank: " + bestModel.rank());
        System.out.println("MSE compute over test set: " + MSE);

        // Saving best model
        bestModel.save(jsc.sc(), modelPath);

        // Stopping context
		jsc.stop();
    }

}
