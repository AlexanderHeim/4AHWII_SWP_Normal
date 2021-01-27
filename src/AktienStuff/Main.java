package AktienStuff;

import AktienStuff.Database.DatabaseManager;
import AktienStuff.Database.MySQL.StockBaseMYSQL;
import AktienStuff.GUI.ChartFrame;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static String api_key = "";

    public static void main(String[] args) throws IOException {
        api_key = Utility.getApiKeyFromFile("AlphaVantageKey.txt");

        StockBaseMYSQL base = new StockBaseMYSQL("java", "password", "aktienstuff", "jdbc:mysql://localhost:3306/aktienstuff");
        DatabaseManager dbm = new DatabaseManager(base);

        dbm.open_connection();
        //dbm.reset_database();
        //dbm.update("IBM", list);
        dbm.close_connection();

        ChartFrame chartFrame = new ChartFrame();
    }
}
