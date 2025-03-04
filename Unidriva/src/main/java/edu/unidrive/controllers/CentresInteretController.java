package edu.unidrive.controllers;
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

public class CentresInteretController {

    @FXML
    private TextField centresInteretField;

    @FXML
    private Button btnadmin;

    private final PostService postService = new PostService();

    private Connection connection;

    private String jwtToken;

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
        checkAdminAccess();
    }

    private boolean isAdmin(String token) {
        try {
            Claims claims = JwtUtil.validateToken(token);
            String role = claims.get("role", String.class);
            return "ADMIN".equals(role);
        } catch (Exception e) {
            return false;
        }
    }

    public void checkAdminAccess() {
        if (jwtToken != null) {
            boolean isAdmin = isAdmin(jwtToken);
            btnadmin.setVisible(isAdmin); // Afficher ou masquer le bouton Admin en fonction du rôle
        } else {
            btnadmin.setVisible(false); // Masquer le bouton Admin si l'utilisateur n'est pas authentifié
        }
    }

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

    @FXML
    void admin(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("AdminInterface.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnadmin.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}