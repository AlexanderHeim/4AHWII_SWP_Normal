package utils;

import java.io.IOException;
import java.util.HashMap;

public class WhenToWork {
    public static void main(String[] args) throws IOException {
        System.out.println(getFreeDaysBetweenYears("BY", 2017, 2020));

    }

    private static HashMap<String, Integer> getFreeDays(String statecode, int year) throws IOException {
        HashMap<String, Integer> map1 = FeiertagAPI.getFreeDaysInYear(statecode, year);
        HashMap<String, Integer> map2 = FerienAPI.getFreeDaysInYear(statecode, year);
        for (String key : map1.keySet()) {
            if (map2.get(key) != null) {
                map2.put(key, map1.get(key) + map2.get(key));
            } else {
                map2.put(key, map2.get(key));
            }
        }
        return map2;
    }

    private static HashMap<String, Integer> getFreeDaysBetweenYears(String statecode, int start_year, int end_year) throws IOException {
        HashMap<String, Integer> map1 = FeiertagAPI.getFreeDaysBetweenYears(statecode, start_year, end_year);
        HashMap<String, Integer> map2 = FerienAPI.getFreeDaysBetweenYears(statecode, start_year, end_year);
        for (String key : map1.keySet()) {
            if (map2.get(key) != null) {
                map2.put(key, map1.get(key) + map2.get(key));
            } else {
                map2.put(key, map2.get(key));
            }
        }
        return map2;
    }
}
