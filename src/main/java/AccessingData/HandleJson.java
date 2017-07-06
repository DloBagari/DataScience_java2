package AccessingData;




import com.fasterxml.jackson.jr.ob.JSON;

import java.io.IOException;
import java.util.Map;

/**
 *Handling json file with jackson library
 */
public class HandleJson {
    public static void main(String[] args) throws IOException {
        String text = "java programing";
        // use md5.jsontext.com py passing a text parameter to return a hash of the text, as json
        String json = HandleHTML.request("http://md5.jsontest.com/?text=" +
                text.replace(" ", ""));
        System.out.println(json);
        //convert json to a map, using parser
        Map<String, Object> map = JSON.std.mapFrom(json);
        System.out.println(map.get("original"));
        System.out.println(map.get("md5"));
    }
}
