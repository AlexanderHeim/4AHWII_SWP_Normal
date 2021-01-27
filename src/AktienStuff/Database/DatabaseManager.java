package AktienStuff.Database;

import AktienStuff.Database.Interfaces.IStocksDatabase;
import AktienStuff.Database.MySQL.StockBaseMYSQL;
import AktienStuff.Equity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class DatabaseManager {

    public IStocksDatabase database;

    public DatabaseManager(IStocksDatabase db_interface) {
        this.database = db_interface;
    }

    public boolean open_connection() {
        return this.database.open_connection();
    }

    public boolean close_connection() {
        return this.database.close_connection();
    }

    public boolean update(String equity_name, List<Equity> equities) {
        if(!this.database.is_connection_open()) return false;

        if(!this.database.does_timeseries_table_exist(equity_name)) {
            this.database.create_timeseries_table(equity_name);
        }
        if(!this.database.does_200avg_table_exist(equity_name)) {
            this.database.create_200avg_table(equity_name);
        }

        for(Equity equity : equities) {
            if (database.does_equity_exist_by_date(equity_name, (Date) equity.getDate())) {
                database.delete_equity_by_date(equity_name, (Date) equity.getDate());
            }
            database.insert_equity_into_table(equity_name, equity);
        }
        for(int i = 0; i < equities.size(); i++) {
            BigDecimal avg = database.calculate_200avg_for_date(equity_name, (Date) equities.get(i).getDate());
            if(database.does_200avg_exist_by_date(equity_name, (Date) equities.get(i).getDate())) {
                database.delete_200avg_by_date(equity_name, (Date) equities.get(i).getDate());
            }
            database.insert_200avg_into_table(equity_name, (Date) equities.get(i).getDate(), avg);
        }
        return true;
    }

    public void reset_database() {
        database.reset_database();
    }

    public HashMap<Date, Equity> get_whole_timeseries(String equity_name) {
        if(!this.database.is_connection_open()) return null;
        if(!this.database.does_timeseries_table_exist(equity_name)) return null;
        return database.get_equities_between_dates(equity_name, Date.valueOf("0000-01-01"), Date.valueOf("9999-01-01"));
    }

    public HashMap<Date, Equity> get_last_x_timeseries_entries(String equity_name, int amount) {
        if(!this.database.is_connection_open()) return null;
        if(!this.database.does_timeseries_table_exist(equity_name)) return null;
        return database.get_equities_between_dates(equity_name, Date.valueOf(LocalDate.now().minusDays(amount)), Date.valueOf(LocalDate.now()));
    }

    public HashMap<Date, BigDecimal> get_whole_200avgs(String equity_name) {
        if(!this.database.is_connection_open()) return null;
        if(!this.database.does_timeseries_table_exist(equity_name)) return null;
        return database.get_200avgs_between_dates(equity_name, Date.valueOf("0000-01-01"), Date.valueOf("9999-01-01"));
    }
}
