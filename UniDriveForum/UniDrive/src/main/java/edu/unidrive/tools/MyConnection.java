package edu.unidrive.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private String url="jdbc:mysql://localhost:3306/unidrive";
    private String login="root";
    private String pwd="";
    private Connection cnx;
    private static MyConnection instance;


    public static MyConnection getInstance() {
        return instance;
    }

    public String getUrl() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getPwd() {
        return pwd;
    }

    public Connection getCnx() {
        return cnx;
    }

    private MyConnection()  {
        try {
            cnx= DriverManager.getConnection(url,login,pwd);
            System.out.println("connection established");
        } catch (SQLException e) {
           System.out.println("error,connection not established!" +e.getMessage());        }

    }
}
