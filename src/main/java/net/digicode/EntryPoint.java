package net.digicode;

import com.jayway.jsonpath.PathNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;


public class EntryPoint {

    public final static String TARGET_URL = "http://api.goeuro.com/api/v2/position/suggest/en/";

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Wrong number of arguments: "
                    + args.length + ", URL should be specified");
        }

        JsonToCsvWriter writer = createWriter("/app.properties");
        String fullUrl = TARGET_URL + args[0];
        try {
            writer.writeJsonToCSV(new URL(fullUrl));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Url is not correct: " + fullUrl);
        }
    }

    private static JsonToCsvWriter createWriter(String propertyFileName) {
        Config config = new Config(propertyFileName);
        return new JsonToCsvWriter(config);
    }
}
