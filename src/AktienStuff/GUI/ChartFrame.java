package AktienStuff.GUI;


import AktienStuff.AlphaVantage;
import AktienStuff.Database.DatabaseManager;
import AktienStuff.Database.MySQL.StockBaseMYSQL;
import AktienStuff.Equity;
import AktienStuff.Main;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChartFrame extends JFrame {

    private JMenuBar menubar;
    private ChartPanel chartpanel;

    public ChartFrame() {
        super("Aktienstuff");
        this.setSize(720, 500);
        this.menubar = createMenuBar();
        this.setJMenuBar(this.menubar);
        this.setVisible(true);
    }

    public void change_timeseries_with_avgs(String name, HashMap<Date, Equity> timeseries, HashMap<Date, BigDecimal> avgs) {
        JFreeChart jChart = new_timeseries_200avg_chart(name, timeseries, avgs);
        if(this.chartpanel != null) {
            this.remove(this.chartpanel);
        }
        Date largest = (Date) timeseries.keySet().toArray()[0];
        for (Date d : timeseries.keySet()) {
            if (d.compareTo(largest) > 0) {
                largest = d;
            }
        }
        if(timeseries.get(largest).getClose().compareTo(avgs.get(largest)) < 0 ) {
            jChart.getPlot().setBackgroundPaint( Color.decode("#b84949"));
        } else {
            jChart.getPlot().setBackgroundPaint( Color.decode("#2cab4e"));
        }

        this.chartpanel = new ChartPanel(jChart);
        chartpanel.setMaximumDrawWidth(4000);
        chartpanel.setMaximumDrawHeight(4000);
        this.menubar.getMenu(0).getItem(0).setText("Current: " + name);
        this.chartpanel.setChart(jChart);
        this.add(chartpanel);
        this.revalidate();
        this.repaint();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Equity");
        menuBar.add(menu);
        JMenuItem menuItem1 = new JMenuItem("Current: ");
        JMenuItem menuItem2 = new JMenuItem("Change Equity");
        JMenuItem menuItem3 = new JMenuItem("Large refetch");
        menuItem2.addActionListener(new ChangeEquityListener());
        menuItem3.addActionListener(new LargeRefetchListener());
        menu.add(menuItem1);
        menu.add(menuItem2);
        menu.add(menuItem3);
        return menuBar;
    }

    private JFreeChart new_timeseries_200avg_chart(String name, HashMap<Date, Equity> timeseries, HashMap<Date, BigDecimal> avgs) {
        TimeSeries ts1 = new TimeSeries(name);
        for (Date d : timeseries.keySet()) {
            Equity equity = timeseries.get(d);
            LocalDate date = d.toLocalDate();
            int day = date.getDayOfMonth();
            int month = date.getMonthValue();
            int year = date.getYear();
            ts1.add(new Day(day, month, year), equity.getClose());
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

    public TimeSeriesCollection create_dataset(HashMap<Date, Equity> timeseries) {
        TimeSeries ts = new TimeSeries("TEST");
        for (Date d : timeseries.keySet()) {
            Equity e = timeseries.get(d);
            int day = d.toLocalDate().getDayOfMonth();
            int month = d.toLocalDate().getMonthValue();
            int year = d.toLocalDate().getYear();
            ts.add(new Day(day, month, year), e.getClose());
        }
        return new TimeSeriesCollection(ts);
    }


    class ChangeEquityListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String new_equity_name = JOptionPane.showInputDialog(null, "Insert equity name", null);
            try {
                List<Equity> list = AlphaVantage.getAdjustedTimeSeries(new_equity_name, Main.api_key, false);

                StockBaseMYSQL base = new StockBaseMYSQL("java", "password", "aktienstuff", "jdbc:mysql://localhost:3306/aktienstuff");
                DatabaseManager dbm = new DatabaseManager(base);
                dbm.open_connection();
                if (!dbm.database.does_timeseries_table_exist(new_equity_name)) {
                    dbm.update(new_equity_name, list);
                }
                HashMap<Date, Equity> equities1 = dbm.get_whole_timeseries(new_equity_name);
                HashMap<Date, BigDecimal> avgs1 = dbm.get_whole_200avgs(new_equity_name);
                dbm.close_connection();

                change_timeseries_with_avgs(new_equity_name, equities1, avgs1);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    class LargeRefetchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                List<Equity> list = AlphaVantage.getAdjustedTimeSeries(chartpanel.getChart().getTitle().getText(), Main.api_key, false);
                StockBaseMYSQL base = new StockBaseMYSQL("java", "password", "aktienstuff", "jdbc:mysql://localhost:3306/aktienstuff");
                DatabaseManager dbm = new DatabaseManager(base);
                dbm.open_connection();
                dbm.update(chartpanel.getChart().getTitle().getText(), list);
                HashMap<Date, Equity> equities1 = dbm.get_whole_timeseries(chartpanel.getChart().getTitle().getText());
                HashMap<Date, BigDecimal> avgs1 = dbm.get_whole_200avgs(chartpanel.getChart().getTitle().getText());
                dbm.close_connection();

                change_timeseries_with_avgs(chartpanel.getChart().getTitle().getText(), equities1, avgs1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    
}
