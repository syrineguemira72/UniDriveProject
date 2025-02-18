package ACT.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private String url = "jdbc:mysql://localhost:3306/hamza_pi"; // Database URL
    private String login = "root"; // Username
    private String pwd = ""; // Password
    private Connection cnx;
    private static MyConnection instance;

    // Private constructor for Singleton pattern
    public MyConnection() {
        try {
            // Attempt to establish the connection
            cnx = DriverManager.getConnection(url, login, pwd);
            System.out.println("Connection established!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage()); // Print the error message
        }
    }

    // Singleton instance access method
    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    // Method to get the current connection
    public Connection getCnx() {
        return cnx;
    }

    public Connection getConnection() {
        return null;
    }
}
