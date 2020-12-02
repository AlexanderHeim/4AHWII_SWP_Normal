package AktienStuff;

import Utils.JsonUtilities;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class AlphaVantage {

    public static ArrayList<Equity> getAdjustedTimeSeries(String equity_name, String api_key) throws IOException {
        ArrayList<Equity> timeSeries = new ArrayList<>();
        String request_url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + equity_name + "&apikey=" + api_key;
        JSONObject series = (JSONObject) JsonUtilities.getJsonFromURL(request_url).get("Time Series (Daily)");
        String[] keys = JSONObject.getNames(series);
        for (String key : keys) {
            JSONObject value = (JSONObject) series.get(key);
            timeSeries.add(new Equity(value, Date.valueOf(key)));
        }
        return timeSeries;
    }
}
