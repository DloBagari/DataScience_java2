package AccessingData;

import joinery.DataFrame;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by dlo on 05/07/17.
 */
public class StoreDataInMapDB {
    public static void main(String[] args) throws IOException{
        Crawler crawler = new Crawler();
        //create a MapDB, is implemented map interface and it supported b a file on disk.
        DB db = DBMaker.fileDB("urls.db").closeOnJvmShutdown().make();
        HTreeMap<?, ?> hTreeMap = db.hashMap("urls").createOrOpen();
        //since HTreeMap is implemented map we can cast it to use map methods on ThreeMap
        Map<String, String> urls = (Map<String, String>)hTreeMap;
        Path path = Paths.get("Data/search-results.txt");
        //read file and store lines in a list
        List<String> lines = FileUtils.readLines(path.toFile(), StandardCharsets.UTF_8);
        //process each like in parallel, apply to map on each line (chain map)
        lines.parallelStream().map(line -> line.split("\t"))
                .map(split ->"http://" + split[2]).distinct().forEach(url -> {
                    try {
                        System.out.println(url);
                        Optional<String> html = crawler.crawl(url);
                        if (html.isPresent()) {
                            urls.put(url, html.get());
                        }
                    } catch (Exception ex) {
                        System.err.println("exception on : " + url);
                    }
        });
        crawler.shutdown();
        System.out.println(urls.size());
        //for each page we look up its html, if its not crawled we skip  the page, then parse the html page
        //flatMap: Returns a stream consisting of the results of replacing each element of this stream with
        // the contents of a mapped stream produced by applying the provided mapping function to each element.
        Stream<PageDetails> pages = lines.parallelStream().flatMap(line -> {
           String[] split = line.split("\t");
           String query = split[0];
           int position = Integer.parseInt(split[1]);
           //convert position to page number
           int searchPageNumber = 1 + (position -1) / 10;
           String url = "http://" + split[2];
           if (!urls.containsKey(url)) {
               //url not crawled
               return Stream.empty();
           }
           PageDetails page = new PageDetails(url, position, searchPageNumber);
           String html = urls.get(url);
            Document document = Jsoup.parse(html);
            String title = document.title();
            int titleLength = title.length();
            page.setTitleLength(titleLength);
            boolean queryTitle = title.toLowerCase().contains(query.toLowerCase());
            page.setQueryInTitle(queryTitle);
            if (document.body() == null) {
                //no body for this document
                return Stream.empty();
            }
            int bodyLength = document.body().text().length();
            page.setBodyContentLength(bodyLength);
            int numberOfLinks = document.body().select("a").size();
            page.setNumberOfLinks(numberOfLinks);
            int numberOfHeaders = document.body().select("h1,h2,h3,h4,h5,h6").size();
            page.setNumberOfHeaders(numberOfHeaders);
            return Stream.of(page);
        });
        //build json file from objects
        try(FileWriter fileWriter = new FileWriter("Data/pageJson.json")) {
            pages.forEach(pageDetails -> {
                try {
                    fileWriter.append(pageDetails.toJson() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
