package edu.unidrive.controllers;
import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.PostService;
import edu.unidrive.tools.JwtUtil;
import edu.unidrive.tools.MyConnection;
import io.jsonwebtoken.Claims;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CentresInteretController {

    @FXML
    private TextField centresInteretField;
    private String currentUserEmail;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        System.out.println("Email of the logged in user: " + currentUserEmail);
    }
    private Connection connection;




    private final PostService postService = new PostService();






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
        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            return 52; // Fallback si email non trouvé
        }

        String query = "SELECT id FROM utilisateur WHERE email = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, currentUserEmail);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id"); // Retourne le vrai ID
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
        return 52; // Fallback en cas d'erreur
    }



}