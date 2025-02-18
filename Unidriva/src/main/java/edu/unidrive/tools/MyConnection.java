package edu.unidrive.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private static MyConnection instance;
    private Connection cnx;

    private final String URL = "jdbc:mysql://localhost:3306/unidrive";
    private final String USER = "root";
    private final String PASSWORD = "";

    private MyConnection() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion à la base de données réussie !");
        } catch (SQLException e) {
            System.out.println(" Erreur de connexion à la base de données : " + e.getMessage());
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
