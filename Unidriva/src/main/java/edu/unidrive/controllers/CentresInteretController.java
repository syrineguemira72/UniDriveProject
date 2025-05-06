package edu.unidrive.controllers;

import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.PostService;
import edu.unidrive.services.UserService;
import edu.unidrive.tools.MyConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;


public class CentresInteretController {

    @FXML
    private TextField centresInteretField;

    private final PostService postService = new PostService();

    private Connection connection;


    @FXML
    void validerCentresInteret() {
        String centresInteret = centresInteretField.getText().trim();
        this.connection = MyConnection.getInstance().getCnx();
        if (centresInteret.isEmpty()) {
            // Afficher un message d'erreur si le champ est vide
            System.out.println("Veuillez entrer au moins un centre d'intérêt.");
            return;
        }

        // Enregistrer les centres d'intérêt dans la base de données
        int userId = getCurrentUserId(); // Récupérer l'ID de l'utilisateur connecté
        postService.saveUserInterests(userId, centresInteret);

        // Rediriger l'utilisateur vers l'interface principale
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePost.fxml"));
            Parent root = fxmlLoader.load();
            centresInteretField.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface principale : " + e.getMessage());
        }
    }

    private int getCurrentUserId() {
        return 40;
    }


}