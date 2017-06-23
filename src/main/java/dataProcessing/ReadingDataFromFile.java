package dataProcessing;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reading input data from a file
 * data is usucally in text format  txt, scv, json
 */
public class ReadingDataFromFile {
    /**
     * read whole file and store each line in a arrayList
     * using BufferedReader
     */
    public static void main(String[] args) throws IOException {
        List<String> lines = new ArrayList<>();
        try(InputStream inputStream = new FileInputStream("Data/file1.txt")) {
            // its important to provides character encoding,to let reader knows how to
            // translate the sequence of bytes into proper String object
            // uft-8 is ascii based text encoding for english
            try(InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    StandardCharsets.UTF_8)) {
                try(BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                    inputStreamReader.close();
                }
            }
        }
        // display the context of the array
        for (String s:lines)
            System.out.println(s);

        // shortcut to get BufferedReader for a file directly
        Path path = Paths.get("Data/file1.txt");
        try (BufferedReader reader2 = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            String line = null;
            lines.clear();
            while ((line = reader2.readLine()) != null) {
                lines.add(line);
            }
            for (String s:lines)
                System.out.println(s);
        }
        // we can make this task easier by using java NIO API, nio.file.Files.readAllLines(path, charSet)
        // which returns list of all lines on the file. this works only with files
        // does not work with inputStream objects
    }
}
