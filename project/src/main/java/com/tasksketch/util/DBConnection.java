package com.tasksketch.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java — Utility class for database connectivity.
 * Provides a static method to get a MySQL connection via JDBC.
 *
 * ⚠️ UPDATE the URL, USER, and PASSWORD below to match your MySQL setup.
 */
public class DBConnection {

    // ----- Database Configuration -----
    private static final String URL = "jdbc:mysql://localhost:3306/tasksketch_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "tasksketch";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    // Load the MySQL JDBC driver once when the class is loaded
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    /**
     * Returns a new connection to the MySQL database.
     * The caller is responsible for closing this connection.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
