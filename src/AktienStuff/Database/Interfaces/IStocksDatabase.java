package AktienStuff.Database.Interfaces;

import AktienStuff.Equity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;

public interface IStocksDatabase {


    boolean open_connection();

    boolean close_connection();

    boolean is_connection_open();


    boolean create_timeseries_table(String name);

    boolean drop_timeseries_table(String name);

    boolean does_timeseries_table_exist(String name);

    boolean insert_equity_into_table(String table_name, Equity equity);

    boolean does_equity_exist_by_date(String table_name, Date date);

    boolean delete_equity_by_date(String table_name, Date date);

    Equity get_equity_by_date(String table_name, Date date);

    HashMap<Date, Equity> get_equities_between_dates(String table_name, Date start_date, Date end_date);

    boolean delete_equities_between_dates(String table_name, Date start_date, Date end_date);


    boolean create_200avg_table(String name);

    boolean drop_200avg_table(String name);

    boolean does_200avg_table_exist(String name);

    BigDecimal calculate_200avg_for_date(String equity_name, Date date);

    boolean insert_200avg_into_table(String table_name, Date date, BigDecimal openvalue);

    boolean delete_200avg_by_date(String table_name, Date date);

    boolean does_200avg_exist_by_date(String table_name, Date date);

    BigDecimal get_200avg_by_date(String table_name, Date date);

    HashMap<Date, BigDecimal> get_200avgs_between_dates(String table_name, Date start_date, Date end_date);

    boolean reset_database();
}
