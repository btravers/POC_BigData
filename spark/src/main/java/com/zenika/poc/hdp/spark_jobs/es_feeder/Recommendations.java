package com.zenika.poc.hdp.spark_jobs.es_feeder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Recommendations {

    /**
     * Main function that retrieve recommendations for a given user using a given model
     *
     * Arguments:
     *  - model path
     *  - user id
     *  - number of suggestions
     *  - ES host
     *
     * @param args
     * @throws Exception
     *
     * $SPARK_HOME/bin/spark-submit --class com.zenika.poc.hdp.spark_jobs.es_feeder.Recommendations /spark_jobs-1.0-SNAPSHOT-jar-with-dependencies.jar file:/data/model1m 6000 100 172.17.0.66:9200
     */
    public static void main(String... args) throws Exception {
        if (args.length != 4) {
            throw new Exception("Expecting three arguments");
        }

        // Reading arguments: the model path, the user id, the number of suggestions, the recommendations destination path
        String modelPath = args[0];
        int nbUser = Integer.parseInt(args[1]);
        int nbMovies = Integer.parseInt(args[2]);
        String es = args[3];

        // Context initialization
        SparkConf sparkConf = new SparkConf().setAppName("ALS item suggestions");
        sparkConf.set("es.index.auto.create", "false");
        sparkConf.set("es.nodes", es);
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // Loading previously computed model
        MatrixFactorizationModel model = MatrixFactorizationModel.load(jsc.sc(), modelPath);

        for (int user=1; user<=nbUser; user++) {
            // Computing recommendations for the given user id
            Rating[] recommendations = model.recommendProducts(user, nbMovies);

            JavaRDD<Map<String, Object>> recommendationsRDD = jsc.parallelize(Arrays.asList(recommendations)).map(new Function<Rating, Map<String, Object>>() {
                @Override
                public Map<String, Object> call(Rating v1) throws Exception {
                    Map<String, Object> recommendation = new HashMap<>();

                    recommendation.put("movie", v1.product());
                    recommendation.put("user", v1.user());
                    recommendation.put("mark", v1.rating());

                    return recommendation;
                }
            });

            // Saving recommendations result
            JavaEsSpark.saveToEs(recommendationsRDD, "library/recommendation");
        }

        // Stopping context
        jsc.stop();
    }
}
