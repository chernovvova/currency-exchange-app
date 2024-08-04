package ru.chernov.currencyexchangeapp.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String DRIVER_NAME = "org.sqlite.JDBC";
    private Connection connection = null;

    public DataBaseConnection() {
        if(connection == null){
            try {
                Class.forName(DRIVER_NAME);
                connection = DriverManager.getConnection("jdbc:sqlite:/Users/vova/IdeaProjects/currency-exchange-app/currency_exchange_app.sqlite");

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
