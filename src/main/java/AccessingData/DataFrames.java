package AccessingData;

import cyclops.async.LazyReact;
import joinery.DataFrame;
import org.jooq.lambda.tuple.Tuple2;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *DataFrame is the way to represent tabular data in memory.
 * is originally
 *  came from R programming, the need of dataFrames comes because of the collections in java can be represented
 *  as tables but we can only access to data with rows index, but in data frames we can access with columns as well.
 *  data frames allow us to apply the same function over all values in the same columns,
 *  using joinery library which is not on maven central. we need to add repository for this library
 */
public class DataFrames {
    public static void main(String[] args) throws IOException {
        DataFrame<Object> dataFrame = DataFrame.readCsv("Data/csvdata.csv");
        //System.out.println(dataFrame);
        //we can access very row and every columns in dataFrame, given column name joinery returns a list of values
        List<Object> country = dataFrame.col("country");
        //System.out.println(country);
        //associate every country that we have with a unique index
        Map<String, Long> mapCountry =
                LazyReact.sequentialBuilder()
                        .from(country)
                        .cast(String.class)
                        .distinct().zipWithIndex()
                        .toMap(Tuple2::v1, Tuple2::v2);
        System.out.println(mapCountry);
        List<Object> indexes = country.stream().map(mapCountry::get).collect(Collectors.toList());
        System.out.println(indexes);

        //drop country column and add country index column
        dataFrame = dataFrame.drop("country");
        dataFrame.add("country_index", indexes);
        System.out.println(dataFrame);
        //we can do much more with joinery: group by, join,...

    }

}
