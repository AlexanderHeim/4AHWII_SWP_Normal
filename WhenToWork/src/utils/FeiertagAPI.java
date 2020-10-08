package utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.HashMap;

public class FeiertagAPI {

    public static HashMap<String, Integer> getFreeDaysInYear(String state, int year) throws IOException {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        JSONObject jsonObj = JsonUtilities.getJsonFromURL("https://feiertage-api.de/api/?jahr=" + year + "&nur_land=" + state);
        String[] keys = JSONObject.getNames(jsonObj);
        for (String key : keys) {
            JSONObject value = (JSONObject) jsonObj.get(key);
            String day = LocalDate.parse(value.get("datum").toString()).getDayOfWeek().toString();
            if (map.get(day) != null) {
                map.put(day, map.get(day) + 1);
            } else {
                map.put(day, 1);
            }

        }
        return map;
    }

    public static HashMap<String, Integer> getFreeDaysBetweenYears(String state, int start_year, int end_year) throws IOException {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for(int year = start_year; year <= end_year; year++){
            HashMap<String, Integer> new_map = getFreeDaysInYear(state, year);
            for (String key : new_map.keySet()) {
                if (map.get(key) != null) {
                    map.put(key, map.get(key) + new_map.get(key));
                } else {
                    map.put(key, new_map.get(key));
                }
            }
        }
        return map;
    }
}
