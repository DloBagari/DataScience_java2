package dataProcessing;

import org.apache.commons.io.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class ApacheCommonsIOUtils {
    public static void main(String[] args)throws IOException {
        //reading bytes from a URL, and printing them
        //typical way is:
        try (InputStream inputStream = new URL(" http://commons.apache.org").openStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            //process thr reader
        }
        //IOUtils contains utility methods dealing with reading, writing and copying.
        //The methods work on InputStream, OutputStream, Reader and Writer
        //with IOUtils could be done with:
        try (InputStream inputStream = new URL( "http://commons.apache.org").openStream()) {
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            //process content
        }
        //Using the above technique to read a 1GB file would result in an attempt to create a 1GB String object!

        // The FileUtils class contains utility methods for working with File objects.
        // These include reading, writing, copying and comparing files.
        //read an entire file line by line you could use:
        File file = new File("Data/file1.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        // lot of operations can be applied to a file using FileUtils , copy , write, move , delete, compare,...

        // The FilenameUtils class contains utility methods for working with filenames without using File objects.
        String fileName = "Data/file1.txt";
        String fullPath = FilenameUtils.getFullPath(fileName);
        String fileNameExtension = FilenameUtils.getExtension(fileName);
        System.out.println(fileNameExtension);
        System.out.println(FilenameUtils.normalize(fileName));

        // The FileSystemUtils class contains utility methods for working with the file system
        // to access functionality not supported by the JDK. Currently, the only method
        // is to get the free space on a drive. Note that this uses the command line, not native code.
        // command df -h will list all drives where we can find path
         long dirveFreeSpace = FileSystemUtils.freeSpaceKb("/dev/sda1");
         System.out.println((dirveFreeSpace / 1000 ) + " MB");

        // creating line iterator
        // The org.apache.commons.io.LineIterator class provides a flexible way for working with
        // a line-based file. An instance can be created directly, or via factory methods on FileUtils or IOUtils

        LineIterator iter = FileUtils.lineIterator(file,"utf-8");
        try {
            while (iter.hasNext())
                System.out.println(iter.nextLine());
        }finally {
            LineIterator.closeQuietly(iter);
        }

        // file deleteStrategy
        // FileDeleteStrategy.FORCE:The singleton instance for forced file deletion, which always deletes,
        // even if the file represents a non-empty directory.
        // FileDeleteStrategy.NORMAL :The singleton instance for normal file deletion, which does
        // not permit the deletion of directories that are not empty.
        FileDeleteStrategy deleter = FileDeleteStrategy.FORCE;
        boolean isDeleted = deleter.deleteQuietly(new File("Data/file2_2.txt"));



    }
}
