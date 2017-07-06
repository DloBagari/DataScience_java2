package AccessingData;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * java standard API provide URL, with it we can open InputStream to hold the body of response
 * using IOUtils from commons IO, we can handle the response
 * used library Jsoup to parse HTML pages.
 */
public class HandleHTML {
    public static void main(String[] args) throws IOException {
        //response from request method  is raw html, we may be interested in text in response not the markup
        //we need a library to process the response.
        // Jsoup library:
        //from www.kaggle.com extract information about leader boards
        //data are in table and to extract the data from table we need to find an anchor
        // that uniquely points to this table.
        //using an inspector ( f12 on firefox or chrome)
        //using inspector we can find out the the id of the table which is:
        // css selector: .competition-leaderboard__table
        // the team name is in third column to extract it we need to get the third <td> tag and then look
        //inside its <a> tag , to get the score we target column number 6
        //map to hold team/score
        Map<String, Double> result = new HashMap<>();
        String rawHtml  = request("https://www.kaggle.com/c/avito-duplicate-ads-detection/leaderboard");
        //System.out.println(rawHtml);
        Document document = Jsoup.connect("https://www.kaggle.com/c/avito-duplicate-ads-detection/leaderboard").get();
        //System.out.println(document.toString());
        Elements tableRaw = document.select(".competition-leaderboard__table tr");
        System.out.println(tableRaw.toString());
        for (Element tr: tableRaw) {
            Elements columns = tr.select("td");
            if (columns.isEmpty())
                continue;
            String team = columns.get(2).text();
            double score = Double.parseDouble(columns.get(4).text());
            System.out.println(team + "  " + score);
        }

    }

    public static String request(String url) throws IOException {
        try (InputStream inputStream = new URL(url).openStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
