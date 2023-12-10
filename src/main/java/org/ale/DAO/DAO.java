package org.ale.DAO;

import java.sql.*;
import java.time.Period;
import java.util.ArrayList;

public abstract class DAO {
    private String url;
    protected Connection connection;

    public DAO(String url) {
        this.url = url;
    }

    protected ResultSet execQuery(String query) {
        try {
            connection = connect();
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + url);
        } catch (SQLException e) {
            System.out.println("Error connecting... " + e.getMessage());
        }
        return null;
    }

    protected void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
