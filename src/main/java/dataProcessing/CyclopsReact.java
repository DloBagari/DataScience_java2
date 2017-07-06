package dataProcessing;


import cyclops.async.LazyReact;
import cyclops.stream.FutureStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.jooq.lambda.tuple.Tuple2;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java Streaming API is very powerful way of dealing with data in functional way.
 * the cyclops react library estends this API by adding new operations on streams for more control of the
 * flow execution
 */
public class CyclopsReact {
    public static void main(String[] args) throws IOException {
        // crete a parallel stream from iterator, which id hard to do with standard library.
        //extract all type from file word.txt, and create a map that associates each type with a unique index,
        //for reading data we will use LineIterator from commons IO.
        LineIterator iter = FileUtils.lineIterator(new File("Data/words.txt"), "UTF-8");
        //create executor
        ExecutorService executorService = Executors.newCachedThreadPool();
        FutureStream<String> stream =  LazyReact.parallelBuilder().withExecutor(executorService).from(iter);
        Map<String,Integer> map =stream.map(line -> line.split("\t"))
                .map(arr -> arr[1].toLowerCase())
                .distinct().zipWithIndex().toMap(Tuple2::v1, t -> t.v2.intValue());
        System.out.println(map);
        executorService.shutdown();

    }
}

