package utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class JsonUtilities {
    public static JSONObject getJsonFromURL(URL url) throws IOException {
        return new JSONObject(IOUtils.toString(url, Charset.forName("UTF-8")));
    }

    public static JSONObject getJsonFromURL(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
    }

    public static JSONArray getJsonArrayFromURL(String url) throws IOException {
        return new JSONArray(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
    }
}
