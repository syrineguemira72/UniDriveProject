package edu.unidrive.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private static MyConnection instance;
    private Connection cnx;

    private static final String URL = "jdbc:mysql://localhost:3306/unidrive";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private MyConnection() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Failed to connect to the database: " + e.getMessage());
        }
    }

    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
