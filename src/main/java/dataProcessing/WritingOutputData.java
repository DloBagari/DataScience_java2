package dataProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * writing output data after been processed using PrintWriter class and
 * nio.file.Files.newBufferWriter
 */
public class WritingOutputData {
    //important to remember to always include the encoding, to avoid any error encoding and the default encoding values
    public static void main(String[] args) throws IOException {
        //read Data, process, write data with PrintWriter
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("Data/file1.txt"), StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter("Data/file2_p.txt", "UTF-8")) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String line_p = line.toUpperCase(Locale.ENGLISH);
                writer.println(line_p);
            }
        }

        //read data , process , write with buffered writer
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("Data/file2_p.txt"), StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(Paths.get("Data/file2_2.txt"), StandardCharsets.UTF_8)){
            String line = null;
            while ((line = reader.readLine()) != null) {
                writer.write(line.toLowerCase(Locale.ENGLISH));
                writer.newLine();
            }
        }
    }
}
