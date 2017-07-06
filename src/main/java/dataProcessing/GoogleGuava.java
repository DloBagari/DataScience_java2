package dataProcessing;

import com.google.common.collect.*;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 */
public class GoogleGuava {
    public static void main (String[] args) throws IOException {
        //CharSource is an abstraction for working with any source of character-based data
        File file = new File("Data/words.txt");
        CharSource words = Files.asCharSource(file, StandardCharsets.UTF_8);
        List<String> lines = words.readLines();
        // collections in Guava
        // Lists, Sets
        // Lists.transform(), is like map on stream and it is applied to every element from the list.
        List<Book> books = Lists.transform(lines, line -> {
            String[] parts = line.split("\t");
            return new Book(parts[0], parts[1], 1);
        });
        // the difference between this map from Stream API is that the transform immediately returns a list,
        //so there is no need to first create a stream and then apply map and then collect the result

        // multiSet, multiMap, Table:
        // these are some new collections important to data science,
        // Multiset: are sets where the same element can be stored multiple times,this is usually used for counting
        // things,
        //count the type of books;
        Multiset<String> bookTypes = HashMultiset.create();
        for (Book b: books) {
            bookTypes.add(b.getType());
        }
        // output the results sorted by counts,
        Multiset<String> sortedBookTypes = Multisets.copyHighestCountFirst(bookTypes);
        System.out.println(sortedBookTypes.toString());

        // Multimap: is a map that foe each key can have multiple values, there are several types of multimap
        // two most common maps,
        // listMultimap: this associates a key with a list of values similar to Map<key, List<value>>
        // SetMultiMap: this associates a key with a set of values similar to Set<key, Set<values>>
        // this can be useful for implement group by logic,

        //collect names for each book type;
        ArrayListMultimap<String, String> namesOfTyps = ArrayListMultimap.create();
        for (Book b: books) {
            namesOfTyps.put(b.getType(), b.getName());
        }
        System.out.println(namesOfTyps.toString());

        //its possible to view Multimap as map of collections
        Map<String, Collection<String>> asMap = namesOfTyps.asMap();
        asMap.entrySet().forEach(System.out::println);

        // Table: collection can be seen as two-dimensional extension of the map interface
        // instead of one key, each entry is indexed by to keys, row keys and columns keys
        // its possible to get entire columns using the column key or a row using row key
        // create a table where row is name of the book, col is the type and value of row,col is the number of books
        // which as same name same type.
        //count how many times each( name, type) book appeared in the dataSet
        Table<String, String, Integer> table = HashBasedTable.create();
        for (Book b:books) {
            Integer count = table.get(b.getName(), b.getType());
            if (count == null)
                count = 0;
            table.put(b.getName(), b.getType(), count + 1);
        }
        // once the data is been loaded to table we can access to data with rows or columns
        System.out.println("\n\nTable:\n" + table);
        // get the row 'vitae,'
        Map<String, Integer> vitae = table.row("vitae,");
        System.out.println("\n\nvitae, row\n" + vitae);
        Map<String, Integer> orange = table.column("orange");
        System.out.println("\n\norange column\n" + orange);
        // Guava contains utility classes for working with primitives such as Ints for int, Doubles for double,
        // it can be used to convert a collection of primitive wrappers to primitive array:
        Collection<Integer> values = vitae.values();
        //convert collection of Integer to int[]
        int[] valuesInt = Ints.toArray(values);
        //sum the values in valuesInt
        //we use stream because its fast
        int total = Arrays.stream(valuesInt).sum();
        System.out.println("\ntotal of books with name vitae: " + total);

        // Ordering class
        // A comparator, with additional methods to support common operations.
        /*

         *Acquiring
         The common ways to get an instance of Ordering are:
         Subclass it and implement compare(T, T) instead of implementing Comparator directly
         Pass a pre-existing Comparator instance to from(Comparator)
         Use the natural ordering, natural()

         *Chaining
         Then you can use the chaining methods to get an altered version of that Ordering, including:

         reverse()
         compound(Comparator)
         onResultOf(Function)
         nullsFirst() / nullsLast()

         *Using
         Finally, use the resulting Ordering anywhere a Comparator is required, or use any of its special operations,
         such as:
         immutableSortedCopy(java.lang.Iterable<E>)
         isOrdered(java.lang.Iterable<? extends T>) / isStrictlyOrdered(java.lang.Iterable<? extends T>)
         min(java.util.Iterator<E>) / max(java.util.Iterator<E>)
         understanding the chain in ordering:
         Ordering<Foo> ordering =
               Ordering.natural()
                        .nullsFirst()
                        .onResultOf(getBarFunction)
                        .nullsLast();
        read backwards through a chain
        First, if only one Foo is null, that null value is treated as greater
        Next, non-null Foo values are passed to getBarFunction (we will be comparing Bar values from now on)
        Next, if only one Bar is null, that null value is treated as lesser
        Finally, natural ordering is used (i.e. the result of Bar.compareTo(Bar) is returned)
        */
        // order books by the length of their names.
        // create comparator
        // onResultOf() takes functional lambda or reference
        // as lambda onResultOf(w -> w.getName())
        Ordering<Book> orderByNames = Ordering.natural().onResultOf(Book::getName);
        // immutableSortedCopy(Iterable<E> elements):
        // Returns an immutable list containing elements sorted by this ordering
        List<Book> orderedBookByName = orderByNames.immutableSortedCopy(books);
        System.out.println("ordered books by length name" + orderedBookByName);
        // Returns true if each element in iterable after the first is greater than or equal
        // to the element that preceded it, according to this ordering.
        System.out.println(orderByNames.isOrdered(orderedBookByName));
        // Returns true if each element in iterable after the first is strictly greater than
        // the element that preceded it, according to this ordering.
        System.out.println(orderByNames.isStrictlyOrdered(orderedBookByName));
        // Returns the greater of the two values according to this ordering.
        // If the values compare as 0, the first is returned.
        System.out.println(orderByNames.max(books));
        System.out.println(orderByNames.min(books));
        //Ordering can be used as comparator since it is implemented comparator
        //return the top k and the bottom k element from the list
        //Returns the k least elements from the given iterable according to this ordering,
        // in order from least to greatest.
        System.out.println(orderByNames.leastOf(books, 10));
        //Returns the k greatest elements of the given iterable according to this ordering,
        // in order from greatest to least.
        System.out.println(orderByNames.greatestOf(books, 10));

    }
}
