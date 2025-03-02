package edu.unidrive.controllers;

import edu.unidrive.services.PostService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CentresInteretController {

    @FXML
    private TextField centresInteretField;

    private final PostService postService = new PostService();

    @FXML
    void validerCentresInteret() {
        String centresInteret = centresInteretField.getText().trim();

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
        // Implémentez cette méthode pour récupérer l'ID de l'utilisateur connecté
        return 39; // Exemple : remplacez par la logique réelle
    }
}