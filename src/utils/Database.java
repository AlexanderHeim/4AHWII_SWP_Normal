package utils;

import java.sql.*;
import java.util.HashMap;

public class Database {

    private String url = "datenbanklink";
    private Connection connection = null;

    public Database() throws SQLException {
        this.connect();
        this.createDayTable();
    }

    private Connection getConnection() {
        return this.connection;
    }

    private void connect() throws SQLException {
        this.connection = DriverManager.getConnection(url);
    }

    private void createDayTable() throws SQLException {
        String command = "CREATE TABLE IF NOT EXISTS days (" +
                "id INT NOT NULL AUTO_INCREMENT" +
                "monday INT NOT NULL," +
                "tuesday INT NOT NULL," +
                "wednesday INT NOT NULL," +
                "thursday INT NOT NULL," +
                "friday INT NOT NULL );";

        Statement statement = this.connection.createStatement();
        statement.execute(command);
    }

    public void insertDayData(HashMap<String, Integer> map) throws SQLException {
        String pre_command = "INSERT INTO days(monday, tuesday, wednesday, thursday, friday) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = getConnection().prepareStatement(pre_command);
        preparedStatement.setInt(2, map.get("MONDAY"));
        preparedStatement.setInt(3, map.get("TUESDAY"));
        preparedStatement.setInt(4, map.get("WEDNESDAY"));
        preparedStatement.setInt(5, map.get("THURSDAY"));
        preparedStatement.setInt(6, map.get("FRIDAY"));
        preparedStatement.executeUpdate();
    }

}
