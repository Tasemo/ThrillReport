import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.nio.file.Path;
import java.util.Arrays;

public final class SparkExample {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local")
                .setAppName("SparkExample")
                .set("spark.executor.instances", "1")
                .set("spark.executor.cores", "4");
        JavaSparkContext context = new JavaSparkContext(conf);
        JavaPairRDD<String, Integer> job = context.textFile(Path.of("data/wordcount").toUri().toString(), 4)
                .flatMap(content -> Arrays.asList(content.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey(Integer::sum);
        long start = System.currentTimeMillis();
        job.saveAsTextFile(Path.of("data/spark").toUri().toString());
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Job took " + elapsed + " ms");
    }
}