package edu.unidrive.controllers;

import edu.unidrive.services.UserService;
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

import java.io.IOException;

public class LoginController {

    @FXML
    private Label btnForgot;

    @FXML
    private Button fbbtn;

    @FXML
    private Label lblErrors;

    @FXML
    private Button login;

    @FXML
    private Button signUp;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    void SignUp(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("SignUp.fxml"));
            Parent root = loader.load();

            // Obtenez la scène actuelle et remplacez-la par la scène d'accueil
            Scene scene = new Scene(root);
            Stage stage = (Stage) login.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void login(ActionEvent event) {
        String email = txtUsername.getText();
        String password = txtPassword.getText();


        UserService userService = new UserService();
        boolean isAuthenticated = userService.loginUser(email, password);

        if (isAuthenticated) {
            System.out.println("Bienvenue, " + email + " !");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Profile.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) login.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lblErrors.setText("Nom d'utilisateur ou mot de passe incorrect.");
            lblErrors.setStyle("-fx-text-fill: red;");
        }
    }

}
