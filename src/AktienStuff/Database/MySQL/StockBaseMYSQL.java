package AktienStuff.Database.MySQL;

import AktienStuff.Database.Interfaces.IStocksDatabase;
import AktienStuff.Equity;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;

public class StockBaseMYSQL implements IStocksDatabase {

    private String sql_username;
    private String sql_password;
    private String database_name;
    private String connection_url;

    private Connection connection;

    public StockBaseMYSQL(String sql_username, String sql_password, String database_name, String connection_url) {
        this.sql_username = sql_username;
        this.sql_password = sql_password;
        this.database_name = database_name;
        this.connection_url = connection_url;
    }

    @Override
    public boolean open_connection() {
        if(!is_connection_open()) {
            try {
                this.connection = DriverManager.getConnection(this.connection_url, this.sql_username, this.sql_password);
                return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean close_connection() {
        if (!is_connection_open()) return false;
        try {
            this.connection.close();
            this.connection = null;
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean is_connection_open() {
        return this.connection != null;
    }

    @Override
    public boolean create_timeseries_table(String name) {
        String table_name = "stock_time_series_" + name;
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("create table if not exists " + table_name + " (" +
                    "day date not null primary key," +
                    "open decimal(11,2) not null," +
                    "close decimal(11,2) not null," +
                    "adjusted_close decimal(11,2) not null," +
                    "high decimal(11,2) not null," +
                    "low decimal(11,2) not null," +
                    "volume int not null," +
                    "dividend_amount decimal(12,2) not null," +
                    "split_coefficient decimal(4,2) not null" +
                    ")");
            pstatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean drop_timeseries_table(String name) {
        String table_name = "stock_time_series_" + name;
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("drop table if exists " + table_name);
            pstatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean does_timeseries_table_exist(String name) {
        String table_name = "stock_time_series_" + name;
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("SELECT count(*) FROM information_schema.TABLES WHERE (TABLE_SCHEMA = ?) AND (TABLE_NAME = ?)");
            pstatement.setString(1, database_name);
            pstatement.setString(2, table_name);
            ResultSet set = pstatement.executeQuery();
            set.next();
            return set.getInt(1) != 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insert_equity_into_table(String table_name, Equity equity) {
        String actual_table_name = "stock_time_series_" + table_name;
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("insert into " + actual_table_name +
                    "(day, open, close, adjusted_close, high, low, volume, dividend_amount, split_coefficient) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstatement.setDate(1, (Date) equity.getDate());
            pstatement.setBigDecimal(2, equity.getOpen());
            pstatement.setBigDecimal(3, equity.getClose());
            pstatement.setBigDecimal(4, equity.getAdjusted_close());
            pstatement.setBigDecimal(5, equity.getHigh());
            pstatement.setBigDecimal(6, equity.getLow());
            pstatement.setLong(7, equity.getVolume());
            pstatement.setBigDecimal(8, equity.getDividend_amount());
            pstatement.setBigDecimal(9, equity.getSplit_coefficient());
            pstatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete_equity_by_date(String table_name, Date date) {
        String actual_table_name = "stock_time_series_" + table_name;
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("delete from " + actual_table_name + " where DATE(day) = ?");
            pstatement.setDate(1, date);
            pstatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public Equity get_equity_by_date(String table_name, Date date) {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rset = statement.executeQuery("select * from stock_time_series_" + table_name + " where DATE(day) = '" + date + "'");
            rset.next();
            return new Equity(rset.getDate(1), rset.getBigDecimal(2), rset.getBigDecimal(3),
                    rset.getBigDecimal(4), rset.getBigDecimal(5), rset.getBigDecimal(6),
                    rset.getInt(7), rset.getBigDecimal(8), rset.getBigDecimal(9));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean does_equity_exist_by_date(String name, Date date) {
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("SELECT count(*) FROM stock_time_series_" + name + " WHERE DATE(day) = ?");
            pstatement.setDate(1, date);
            ResultSet set = pstatement.executeQuery();
            set.next();
            return set.getInt(1) != 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public HashMap<Date, Equity> get_equities_between_dates(String table_name, Date start_date, Date end_date) {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rset = statement.executeQuery("select * from stock_time_series_" + table_name + " where day >= '" + start_date + "' and day <= '" + end_date + "'");
            HashMap<Date, Equity> equities = new HashMap<Date, Equity>();
            while(rset.next()) {
                equities.put(rset.getDate(1), new Equity(rset.getDate(1), rset.getBigDecimal(2), rset.getBigDecimal(3),
                        rset.getBigDecimal(5), rset.getBigDecimal(6), rset.getBigDecimal(4),
                        rset.getInt(7), rset.getBigDecimal(8), rset.getBigDecimal(9)));
            }
            return equities;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete_equities_between_dates(String table_name, Date start_date, Date end_date) {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate("delete from stock_time_series_" + table_name + " where day >= '" + start_date + "' and day <= '" + end_date + "'");
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean create_200avg_table(String name) {
        String table_name = "stock_200avg_time_series_" + name;
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("create table if not exists " + table_name + " (" +
                    "day date not null primary key," +
                    "avgopen decimal(11,2) not null)");
            pstatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean drop_200avg_table(String name) {
        String table_name = "stock_200avg_time_series_" + name;
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("drop table if exists " + table_name);
            pstatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public BigDecimal calculate_200avg_for_date(String equity_name, Date date) {
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("select avg(adjusted_close) from stock_time_series_" + equity_name +
                    " where DATE(day) <= ? and DATE(day) >= ?");
            pstatement.setDate(1, date);
            pstatement.setDate(2, Date.valueOf(date.toLocalDate().minusDays(200)));
            ResultSet rset = pstatement.executeQuery();
            rset.next();
            return rset.getBigDecimal(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean does_200avg_table_exist(String name) {
        String table_name = "stock_200avg_time_series_" + name;
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("SELECT count(*) FROM information_schema.TABLES WHERE (TABLE_SCHEMA = ?) AND (TABLE_NAME = ?)");
            pstatement.setString(1, database_name);
            pstatement.setString(2, table_name);
            ResultSet set = pstatement.executeQuery();
            set.next();
            return set.getInt(1) != 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insert_200avg_into_table(String table_name, Date date, BigDecimal openvalue) {
        String actual_table_name = "stock_200avg_time_series_" + table_name;
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("insert into " + actual_table_name +
                    "(day, avgopen) values (?, ?)");
            pstatement.setDate(1, date);
            pstatement.setBigDecimal(2, openvalue);
            pstatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete_200avg_by_date(String table_name, Date date) {
        String actual_table_name = "stock_200avg_time_series_" + table_name;
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("delete from " + actual_table_name + " where DATE(day) = ?");
            pstatement.setDate(1, date);
            pstatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public BigDecimal get_200avg_by_date(String table_name, Date date) {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rset = statement.executeQuery("select * from stock_200avg_time_series_" + table_name + " where DATE(day) = '" + date + "'");
            rset.next();
            return rset.getBigDecimal(2);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean does_200avg_exist_by_date(String name, Date date) {
        try {
            PreparedStatement pstatement = this.connection.prepareStatement("SELECT count(*) FROM stock_200avg_time_series_" + name + " WHERE DATE(day) = ?");
            pstatement.setDate(1, date);
            ResultSet set = pstatement.executeQuery();
            set.next();
            return set.getInt(1) != 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public HashMap<Date, BigDecimal> get_200avgs_between_dates(String table_name, Date start_date, Date end_date) {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rset = statement.executeQuery("select * from stock_200avg_time_series_" + table_name + " where day >= '" + start_date + "' and day <= '" + end_date + "'");
            HashMap<Date, BigDecimal> values = new HashMap<Date, BigDecimal>();
            while(rset.next()) {
                values.put(rset.getDate(1), rset.getBigDecimal(2));
            }
            return values;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean reset_database() {
        try {
            Statement _statement = this.connection.createStatement();
            ResultSet rset = _statement.executeQuery("select table_name from INFORMATION_SCHEMA.TABLES where table_schema = '" + this.database_name + "'");
            while(rset.next()) {
                Statement statement = this.connection.createStatement();
                statement.executeUpdate("drop table " + rset.getString(1));
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
