package org.example;

import ACT.entities.Activite;
import ACT.services.Activite_Crud;
import ACT.tools.MyConnection;

import java.sql.Connection;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        // Get the instance of MyConnection (Singleton)
        MyConnection mc = MyConnection.getInstance();
        Connection connection = mc.getCnx();  // Get the connection object

        // Check if the connection is null before proceeding
        if (connection != null) {
            // Pass the connection to the Activite_Crud constructor
            Activite_Crud myCrud = new Activite_Crud(connection);

            // Create an instance of Activite
            Activite activite = new Activite(
                    0,  // Assuming ID is auto-generated
                    "Randonnée en Montagne", // Nom
                    "Une randonnée de 5 km dans les montagnes.", // Description
                    LocalDateTime.of(2025, 3, 10, 9, 30), // Date et Heure
                    "Montagne des Cèdres" // Lieu
            );

            try {
                // Add the activity to the database
                myCrud.ajouter(activite);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Failed to establish a connection to the database.");
        }
    }
}
