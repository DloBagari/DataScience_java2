package dataProcessing;

import java.util.Arrays;
import java.util.List;

/**
 * Stream operations are easy to parallel, since operations is applied to each item separately
 * we can splits the work across multiple processors and execute all the tasks in parallel
 */
public class ProcessingStreamDataInParallel {
    public static void main(String[] args) {
        Book[] bookList = {new Book("java", "data", 1),
                new Book("java", "object", 2),
                new Book("python", "object", 3),
                new Book("R", "data", 4),
                new Book("sql", "data", 5),
                new Book("python", "network", 6),
                new Book("java", "network", 7),
                new Book("python", "hack", 8)};
        List<Book> list = Arrays.asList(bookList);
        //create a parallelStream, the filtering and mapping is done in parallel, but then the stream is
        //converted to a sequential stream, sorted and top tow element are extracted to an array.
        int[] longestNames = list.parallelStream().filter(book -> book.getName().length() % 2 == 0)
                .map(Book::getName)
                .mapToInt(String::length)
                .sequential()
                .sorted().limit(2)
                .toArray();
        System.out.println(Arrays.toString(longestNames));

    }
}
