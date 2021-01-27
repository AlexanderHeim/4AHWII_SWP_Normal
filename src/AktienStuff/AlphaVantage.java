package AktienStuff;

import Utils.JsonUtilities;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlphaVantage {

    public static List<Equity> getAdjustedTimeSeries(String equity_name, String api_key, boolean compact) throws IOException {
        ArrayList<Equity> timeSeries = new ArrayList<>();
        String size = "compact";
        if (!compact) size = "full";
        String request_url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + equity_name + "&outputsize=" + size + "&apikey=" + api_key;
        JSONObject series = (JSONObject) JsonUtilities.getJsonFromURL(request_url).get("Time Series (Daily)");
        String[] keys = JSONObject.getNames(series);
        for (String key : keys) {
            JSONObject value = (JSONObject) series.get(key);
            timeSeries.add(new Equity(value, Date.valueOf(key)));
        }
        return timeSeries.stream().sorted((Equity e1, Equity e2) -> e1.getDate().compareTo(e2.getDate())).collect(Collectors.toList());
    }
}
