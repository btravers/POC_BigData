package com.zenika.poc.hdp.spark_jobs.recommender;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class LearningCurve {

    final static int NBPOINTS = 50;

    /**
     * Main function that compute learning curve for ALS model using a given rank
     *
     * Aguments:
     *  - data path
     *  - separator
     *  - rank
     *  - curve path
     *
     * @param args
     * @throws Exception
     * 
     * spark-submit --class com.zenika.poc.hdp.spark_jobs.recommender.LearningCurve spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar "hdfs://sandbox.hortonworks.com:8020/poc/data/movielens_1m/ratings.dat" "::" "4" "hdfs://sandbox.hortonworks.com:8020/poc/curve1m"
     */
    public static void main(String... args) throws Exception {
        if (args.length != 4) {
            throw new Exception("Expecting four arguments");
        }

        int numIterations = 20;
        double[] weights = {.8, .2};

        // Reading arguments: the data file path, the data separator, the rank number and result destination path
        String dataPath = args[0];
        final String separator = args[1];
        int rank = Integer.parseInt(args[2]);
        String curvePath = args[3];

        // Context initialization
        SparkConf sparkConf = new SparkConf().setAppName("ALS learning curve");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // Transforming file content into JavaRDD
        JavaRDD<String> data = jsc.textFile(dataPath);

        // Transforming each lines into ratings
        JavaRDD<Rating> ratings = Utils.generateRatings(data, separator);

        JavaRDD<Rating>[] sets = ratings.randomSplit(weights);

        sets[0].cache();
        sets[1].cache();

        long count = sets[0].count();
        long step = count / NBPOINTS;

        List<Tuple2<Double, Double>> curve = new ArrayList<>();

        for (long i = step; i < count; i += step) {
            JavaRDD<Rating> tmp = sets[0].sample(false, i/((double) count));

            MatrixFactorizationModel model = ALS.train(tmp.rdd(), rank, numIterations, 0.01);

            double mse1 = Utils.computeMSE(model, tmp);
            double mse2 = Utils.computeMSE(model, sets[1]);

            curve.add(new Tuple2<>(mse1, mse2));
        }

        // Saving curve
        jsc.parallelize(curve).saveAsTextFile(curvePath);

        // Stopping context
        jsc.stop();
    }
}
