/**
 * Created by xl on 9/18/15.
 */
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import scala.Tuple2;

import java.util.regex.Pattern;

public final class NaiveBayesExample {
        private static class DataToPoint implements Function<String, LabeledPoint> {
            private static final Pattern SPACE = Pattern.compile(",");

            public LabeledPoint call(String line) throws Exception {
                String[] tok = SPACE.split(line);
                double label = Double.parseDouble(tok[tok.length-1]);
                double[] point = new double[tok.length-1];
                for (int i = 0; i < tok.length - 1; ++i) {
                    point[i] = Double.parseDouble(tok[i]);
                }
                return new LabeledPoint(label, Vectors.dense(point));
            }
        }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println(
                    "Usage: NaiveBayesExample <training_data> <test_data>");
            System.exit(1);
        }
        String training_data_path = args[0];
        String test_data_path = args[0];

        SparkConf sparkConf = new SparkConf().setAppName("NaiveBayesExample");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<LabeledPoint> train = sc.textFile(training_data_path).map(new DataToPoint());
        JavaRDD<LabeledPoint> test = sc.textFile(training_data_path).map(new DataToPoint());

        final NaiveBayesModel model = NaiveBayes.train(train.rdd(), 1.0);

        JavaPairRDD<Double, Double> predictionAndLabel =
                test.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
                    public Tuple2<Double, Double> call(LabeledPoint p) {
                        return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
                    }
                });

        double accuracy = predictionAndLabel.filter(new Function<Tuple2<Double, Double>, Boolean>() {
            public Boolean call(Tuple2<Double, Double> pl) {
                return pl._1().equals(pl._2());
            }
        }).count() / (double) test.count();

        System.out.println(accuracy);

        sc.stop();
    }

}
