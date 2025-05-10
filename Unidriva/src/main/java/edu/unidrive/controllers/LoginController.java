package edu.unidrive.controllers;

import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.UserService;
import edu.unidrive.tools.JwtUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

public class LoginController {

    @FXML private Label btnForgot;
    @FXML private Button fbbtn;
    @FXML private Label lblErrors;
    @FXML private Button login;
    @FXML private Button signUp;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtUsername;

    @FXML
    void Forgot(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GetPassword.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnForgot.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void SignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("SignUp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre pour l'inscription
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Inscription");
            stage.show();

            // Fermer la fenêtre de login si nécessaire
            // ((Stage) signUp.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la page d'inscription");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Une erreur inattendue est survenue");
        }
    }

    @FXML

    void login(ActionEvent event) {
        String email = txtUsername.getText().trim();
        String password = txtPassword.getText(); // Ne pas trim()

        System.out.println("=== Login Attempt ===");
        System.out.println("Email: '" + email + "'");
        System.out.println("Password attempt: '" + password + "'");

        UserService userService = new UserService();
        Utilisateur utilisateur = userService.getUserByEmail(email);

        if (utilisateur == null) {
            showError("Email ou mot de passe incorrect.");
            return;
        }

        System.out.println("Stored hash: '" + utilisateur.getPassword() + "'");
        System.out.println("Hash length: " + utilisateur.getPassword().length());
        System.out.println("Verification result: " + BCrypt.checkpw(password, utilisateur.getPassword()));

        if (!BCrypt.checkpw(password, utilisateur.getPassword())) {
            showError("Email ou mot de passe incorrect.");
            return;
        }

        String rolesString = String.join(",", utilisateur.getRoles());
        String token = JwtUtil.generateToken(utilisateur.getEmail(), rolesString);

        if (token == null) {
            lblErrors.setText("Failed to generate authentication token.");
            lblErrors.setStyle("-fx-text-fill: red;");
            return;
        }

        System.out.println("Token généré : " + token);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("HomeUniDrive.fxml"));
            Parent root = loader.load();

            HomeUniDriveController homeController = loader.getController();
            homeController.setCurrentUserEmail(email);
            homeController.setJwtToken(token);

            Stage stage = (Stage) login.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            lblErrors.setText("Failed to load the home page.");
            lblErrors.setStyle("-fx-text-fill: red;");
        }
    }    private String getBanMessage(Utilisateur utilisateur) {
        if (utilisateur.getBanEndDate() == null) {
            return "This ban is permanent.";
        } else {
            return "Ban expires on: " + utilisateur.getBanEndDate().toString();
        }
    }

    private void showError(String message) {
        lblErrors.setText(message);
        lblErrors.setStyle("-fx-text-fill: red;");
    }
}