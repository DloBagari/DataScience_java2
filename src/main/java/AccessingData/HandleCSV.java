package AccessingData;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * using apache commons CSV:
 */
public class HandleCSV {
    public static void main(String[] args) throws IOException {
        List<Person> persons = new ArrayList<>();
        Path path = Paths.get("Data/csvdata.csv");
        //we use bufferedReader , file may not fit to memory
        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        // create CSVFormat, type of csv
        CSVFormat csv = CSVFormat.RFC4180.withHeader();
        // tab-separator is a case of CSV to read tab-sep file use:
        // CSVFormat.TDF.withHeader()
        try (CSVParser parser = csv.parse(reader)) {
            //creating iterator is useful if SCV to large to fit into memory
            //if file is not to big:we can use List<CSVRecord> records = parser.getRecords(),to read entire file at once
            Iterator<CSVRecord> iterator = parser.iterator();
            iterator.forEachRemaining(rec -> {
                String name = rec.get("name");
                String email = rec.get("email");
                String country = rec.get("country");
                double salary = Double.parseDouble(rec.get("salary").substring(1, rec.get("salary").length()));
                int experience = Integer.parseInt(rec.get("experience"));
                persons.add(new Person(name, email, country, salary, experience));
            });
        }

    }
}
