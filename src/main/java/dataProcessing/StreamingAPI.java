package dataProcessing;

import com.sun.corba.se.pept.transport.ListenerThread;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classes to support functional-style operations on streams of elements,
 * such as map-reduce transformations on collections
 * streaming and lambda expression is the most important things in java 8
 * stream is a sequence of objects
 */
public class StreamingAPI {
    public static void main(String[] args) throws IOException {
        Book[] bookList = {new Book("java", "data", 1),
                new Book("java", "object", 2),
                new Book("python", "object", 3),
                new Book("R", "data", 4),
                new Book("sql", "data", 5),
                new Book("python", "network", 6),
                new Book("java", "network", 7),
                new Book("python", "hack", 8)};
        //convert this list to stream of objects
        Stream<Book> stream = Arrays.stream(bookList);
        //steams can be created from collections using stream method
        List<Book> list = Arrays.asList(bookList);
        //Returns a sequential Stream with this collection as its source.
        Stream<Book> stream2 = list.stream();
        //any operation on stream are chained together and form nice and readable data processing pipelines
        //most common operation on streams is filter and map
        //map applies the same transformer function on each element
        //filter given a predicate function, filters out elements that do not satisfy it
        //at the end of the pipeline we can collect the results using a collector
        //the Collectors class provides several implementations such as toList, toSet, toMap...

        //we want books of type data
        //map(ref of a function) Class::methodName
        //get a list of books type where type is data
        //filter: Returns a stream consisting of the elements of this stream that match the given predicate.
        List<String> dataTypeBooks = list.stream().filter(w -> "data".equals(w.getType()))
                .map(Book::getName).collect(Collectors.toList());
        System.out.println(dataTypeBooks.toString());
        //how many type of books are n the list
        Set<String> type_numbers = list.stream().map(Book::getName).collect(Collectors.toSet());
        System.out.printf("types number: %1$d\n", type_numbers.size());
        //join the results (sequence of strings)
        String result = list.stream().filter(book -> "java".equals(book.getName())).map(Book::getType)
                .collect(Collectors.joining(" "));
        System.out.println(result);
        //group by a name
        Map<String, List<Book>> groupByName = list.stream().collect(Collectors.groupingBy(Book::getName));
        System.out.println(groupByName.get("java").toString());
        //index a Book instance using its fields
        //create a map where a field will be key and its object will be value
        //util.function: Functional interfaces provide target types for lambda expressions and method references.
        Map<Integer, Book> indexByField = list.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
        System.out.println(indexByField.get(1).getId());
        //primitive stream, streams of int, double, and another primitive types
        //using mapToInt or mapToDouble , map on each element (run a function which returns primitive data)
        //and then we can do some statical calculations, such as sum, max, min, average
        //find the max length of character of book's name
        //max() return an Optional instance
        int maxNameLength = list.stream().mapToInt(book -> book.getName().length()).max().getAsInt();
        System.out.println(maxNameLength);
        //represent a text file as a stream of lines using a Files.lines
        //flatMap(); return a stream consisting of the results of replacing each element of the stream
        //with the contents of a mapped stream produced by applying the provided mapping function to each element
        //each element will be replaced with the result of the map function on it
        Path path = Paths.get("Data/file1.txt");
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            double average = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .mapToInt(String::length).average().getAsDouble();
            System.out.println(average);
        }

        //File.lines is to read input from a file, if we want to read input from any inputStream
        //like a read lines from a web page or a web socket , using apache common-io, IOUtils
        //IOUtils contains utility methods dealing with reading, writing and copying.
        // The methods work on InputStream, OutputStream, Reader and Writer
        //provides a very helpful utility.
        //read entire input into string or list of strings
        try (InputStream inputStream = new FileInputStream("Data/file1.txt")) {
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            System.out.println("entire file " +content);
        }

        try (InputStream inputStream = new FileInputStream("Data/file1.txt")) {
            List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            System.out.println(lines.toString());
        }
        try {
            String linesWeb = IOUtils.toString(new URI("https://mvnrepository.com/artifact/commons-io/commons-io/2.5"), StandardCharsets.UTF_8);
            System.out.println(linesWeb);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

}
