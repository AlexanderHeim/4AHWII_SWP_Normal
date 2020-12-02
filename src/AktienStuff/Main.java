package AktienStuff;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<Equity> list = AlphaVantage.getAdjustedTimeSeries("IBM", "demo");
        for (Equity equity : list) {
            System.out.println("EQUITY FROM " + equity.getDate().toString());
            System.out.println("CLOSE: " + equity.getClose().toString());
            System.out.println("");
        }
    }
}
