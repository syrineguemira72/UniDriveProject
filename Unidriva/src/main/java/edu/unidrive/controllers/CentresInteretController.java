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
            System.out.println("Veuillez entrer au moins un centre d'intérêt.");
            return;
        }

        int userId = getCurrentUserId();
        postService.saveUserInterests(userId, centresInteret);

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
            return 49;
        }

        String query = "SELECT id FROM utilisateur WHERE email = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, currentUserEmail);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
        return 49;
    }



}