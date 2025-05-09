package edu.unidrive.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private static MyConnection instance;
    private Connection cnx;

    private final String URL = "jdbc:mysql://localhost:3306/unidrive";
    private final String USER = "root";
    private final String PASSWORD = ""; // Replace par votre mot de passe

    public MyConnection() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connect à la base de données!");
        } catch (SQLException e) {
            System.err.println("Check de la connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
            cnx = null;
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