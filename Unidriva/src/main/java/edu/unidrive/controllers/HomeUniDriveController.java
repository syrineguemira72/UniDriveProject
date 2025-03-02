package edu.unidrive.controllers;

import edu.unidrive.tools.JwtUtil;
import io.jsonwebtoken.Claims;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;



import java.io.IOException;

public class HomeUniDriveController {

    @FXML
    private Button Profile;

    @FXML
    private Label logoutbtn;

    @FXML
    private ImageView profileImage;

    @FXML
    private Button statisticsButton;

    private String jwtToken;// Référence à l'ImageView pour afficher la photo de profil

    @FXML
    void Logout(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutbtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String currentUserEmail;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        System.out.println("Email of the logged in user: " + currentUserEmail);
    }

    @FXML
    void goprofile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Profile.fxml"));
            Parent root = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setCurrentUserEmail(currentUserEmail);

            Stage stage = (Stage) Profile.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setProfileImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            profileImage.setImage(image);
        }
    }


    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
        checkAdminAccess(); // Vérifier le rôle de l'utilisateur après avoir défini le token
    }



    private boolean isAdmin(String token) {
        try {
            Claims claims = JwtUtil.validateToken(token);
            String role = claims.get("role", String.class);
            return "ADMIN".equals(role);
        } catch (Exception e) {
            return false; // Le token est invalide ou a expiré
        }
    }

    public void checkAdminAccess() {
        if (jwtToken != null) {
            boolean isAdmin = isAdmin(jwtToken);
            statisticsButton.setVisible(isAdmin); // Afficher ou masquer le bouton en fonction du rôle
        } else {
            statisticsButton.setVisible(false); // Masquer le bouton si l'utilisateur n'est pas authentifié
        }
    }

    @FXML
    void goToStatistics(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Statistics.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Pas de texte d'en-tête
        alert.setContentText(message);
        alert.showAndWait(); // Afficher la boîte de dialogue et attendre une réponse
    }
    @FXML
    void forum(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("HomePost.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutbtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void aide(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CRUD.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutbtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void lost(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Recompense.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutbtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }