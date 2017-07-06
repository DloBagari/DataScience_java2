package AccessingData;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * java DataBase Connectivity JDBC: is an abstraction for accessing any database that supports SQL
 *
 */
public class HandleDataBase {
    public static void main(String[] main) throws SQLException, IOException {
        //read csv file and construct a list of person objects.
        BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("Data/csvdata.csv"));
        CSVFormat csv = CSVFormat.RFC4180.withHeader();
        List<Person> personList = new ArrayList<>();
        try(CSVParser parser = csv.parse(bufferedReader)) {
            Iterator<CSVRecord> iterator = parser.iterator();
            iterator.forEachRemaining(rec -> {
                personList.add(new Person(rec.get("name"),
                        rec.get("email"),
                        rec.get("country"),
                        Double.parseDouble(rec.get("salary").substring(1, rec.get("salary").length())),
                        Integer.parseInt(rec.get("experience"))));
            });
        }
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("javabook");
        dataSource.setUser("root");
        //set the password
        dataSource.setPassword("");
        //using dataSource object we can load the data , there is two ways to load data
        //insert each object individually
        //prepare a batch, and insert all objects of a batch
        List<List<Person>> chunks = Lists.partition(personList, 50);
        //by default is Auto committed
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO people (name, email, country, salary, experience) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (List<Person> chunk:chunks) {
                    for (Person person:chunk) {
                        //indexing start from 1 , not from zero
                        statement.setString(1, person.getName());
                        statement.setString(2, person.getEmail());
                        statement.setString(3, person.getCountry());
                        statement.setDouble(4, person.getSalary());
                        statement.setInt(5, person.getExperience());
                        statement.addBatch();
                    }
                    statement.executeBatch();
                }
            }
        }
        //data is loaded we try to query the data
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT name, email FROM people WHERE country=?;";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "Slovakia");
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString(1) + " " + resultSet.getString(2));
                    }
                }
            }
        }
    }
}

