package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

public class FerienAPI {

    public static HashMap<String, Integer> getFreeDaysInYear(String state, int year) throws IOException {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        JSONArray jsonArr = JsonUtilities.getJsonArrayFromURL("https://ferien-api.de/api/v1/holidays/" + state + "/" + year + ".json");
        System.out.println(jsonArr.toString());
        for(int i = 0; i < jsonArr.length(); i++){
            String start = jsonArr.getJSONObject(i).get("start").toString();
            String end = jsonArr.getJSONObject(i).get("end").toString();
            HashMap<String, Integer> new_map = decodeHolidays(start, end);
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

    private static HashMap<String, Integer> decodeHolidays(String _start, String _end){
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        String start = _start.substring(0, 10);
        String end = _end.substring(0, 10);
        LocalDate dayIterator = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        while(dayIterator.isBefore(endDate.plusDays(1))){
            String day = dayIterator.getDayOfWeek().toString();
            System.out.println(day);
            if (map.get(day) != null) {
                map.put(day, map.get(day) + 1);
            } else {
                map.put(day, 1);
            }
            dayIterator = dayIterator.plusDays(1);
        }
        System.out.println(map.toString());
        return map;
    }
}
