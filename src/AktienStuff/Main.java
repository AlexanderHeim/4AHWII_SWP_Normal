package AktienStuff;

import AktienStuff.Database.DatabaseManager;
import AktienStuff.Database.MySQL.StockBaseMYSQL;
import AktienStuff.GUI.ChartFrame;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static AktienStuff.Utility.new_timeseries_200avg_chart;

public class Main {

    public static String api_key = "";

    public static void main(String[] args) throws IOException {
        boolean no_gui = false;
        for (String s : args) {
            if (s.equalsIgnoreCase("--nogui")) {
                no_gui = true;
            }
        }

        api_key = Utility.getApiKeyFromFile("AlphaVantageKey.txt");
        StockBaseMYSQL base = new StockBaseMYSQL("java", "password", "aktienstuff", "jdbc:mysql://localhost:3306/aktienstuff");
        DatabaseManager dbm = new DatabaseManager(base);

        //dbm.reset_database();
        //dbm.update("IBM", list);

        if (no_gui) {
            dbm.open_connection();
            ArrayList<String> names = Utility.getNamesFromFile("Codes.txt");
            for(String s : names) {
                List<Equity> list = AlphaVantage.getAdjustedTimeSeries(s, Main.api_key, false);
                dbm.update(s, list);
                HashMap<Date, Equity> equities1 = dbm.get_whole_timeseries(s);
                HashMap<Date, BigDecimal> avgs1 = dbm.get_whole_200avgs(s);
                JFreeChart jfc = new_timeseries_200avg_chart(s, equities1, avgs1);
                File f = new File(s + ".png");
                f.createNewFile();
                ChartUtils.saveChartAsPNG(f, jfc, 1000, 500);
            }
            dbm.close_connection();
        } else {
            ChartFrame chartFrame = new ChartFrame();
        }


    }
}
