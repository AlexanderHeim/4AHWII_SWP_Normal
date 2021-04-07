package AktienStuff;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Utility {

    public static String getApiKeyFromFile(String FileName) {

        File file = new File(FileName);

        if (!file.canRead() || !file.isFile()) {
            System.exit(0);
        }
        FileReader fr = null;
        int c;
        StringBuffer buff = new StringBuffer();
        try {
            fr=new FileReader(file);
            while((c = fr.read()) != -1){
                buff.append((char) c);
            }
            fr.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return buff.toString();
    }

    public static ArrayList<String> getNamesFromFile(String FileName) {
        File file = new File(FileName);
        if (!file.canRead() || !file.isFile()) {
            System.exit(0);
        }
        ArrayList<String> names = new ArrayList<String>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line=br.readLine()) != null) {
                names.add(line);
            }
            return names;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JFreeChart new_timeseries_200avg_chart(String name, HashMap<Date, Equity> timeseries, HashMap<Date, BigDecimal> avgs) {
        TimeSeries ts1 = new TimeSeries(name);
        for (Date d : timeseries.keySet()) {
            Equity equity = timeseries.get(d);
            LocalDate date = d.toLocalDate();
            int day = date.getDayOfMonth();
            int month = date.getMonthValue();
            int year = date.getYear();
            ts1.add(new Day(day, month, year), equity.getAdjusted_close());
        }
        TimeSeries ts2 = new TimeSeries("200 avg");
        for (Date d : avgs.keySet()) {
            BigDecimal value = avgs.get(d);
            LocalDate date = d.toLocalDate();
            int day = date.getDayOfMonth();
            int month = date.getMonthValue();
            int year = date.getYear();
            ts2.add(new Day(day, month, year), value);
        }

        TimeSeriesCollection ts_collection = new TimeSeriesCollection();
        ts_collection.addSeries(ts1);
        ts_collection.addSeries(ts2);

        JFreeChart jChart = ChartFactory.createTimeSeriesChart(name, "Date", "Close Value in $", ts_collection, true, true ,false);
        XYPlot plot = (XYPlot) jChart.getPlot();
        plot.getRenderer().setDefaultStroke(new BasicStroke(2.0f));
        ((AbstractRenderer) plot.getRenderer()).setAutoPopulateSeriesStroke(false);
        ((AbstractRenderer) plot.getRenderer()).setSeriesPaint(0,  new Color(0, 0, 0));
        return jChart;
    }
}
