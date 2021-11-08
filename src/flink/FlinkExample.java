import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.nio.file.Path;

public final class FlinkExample {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.createLocalEnvironment(4);
        env.readTextFile(Path.of("data/wordcount").toUri().toString())
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String content, Collector<Tuple2<String, Integer>> collector) {
                        for (String word : content.split(" ")) {
                            collector.collect(new Tuple2<>(word, 1));
                        }
                    }
                })
                .groupBy(0)
                .sum(1)
                .writeAsText(Path.of("data/flink").toAbsolutePath().toString());
        long start = System.currentTimeMillis();
        env.execute();
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Job took " + elapsed + " ms");
    }
}