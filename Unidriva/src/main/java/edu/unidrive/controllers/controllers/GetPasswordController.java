package edu.unidrive.controllers.controllers;

import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GetPasswordController {

    private UserService userService;

    @FXML
    private TextField answertxt;

    @FXML
    private Button backBtn;

    @FXML
    private Label errorAnswer;

    @FXML
    private Label errorLb;

    @FXML
    private Button getpswBtn;

    @FXML
    private TextField nametxt;

    @FXML
    private TextField passtxt;

    @FXML
    private TextField questiontxt;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField usernametxt;

    public GetPasswordController() {
        this.userService = new UserService();
    }


    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void search(ActionEvent event) {
        String username = usernametxt.getText().trim();
        Utilisateur user = userService.getUserByEmail(username);

        if (user == null) {
            errorLb.setText("User not found!");
        } else {
            questiontxt.setText("What is your phone number?");
            nametxt.setText(user.getLastname() + " " + user.getFirstname());
        }
    }

    @FXML
    void RetrivePassword(ActionEvent event) {
        String answer = answertxt.getText().trim();
        String username = usernametxt.getText().trim();

        Utilisateur user = userService.getUserByEmail(username);

        if (user == null) {
            errorLb.setText("User not found!");
            return;
        }

        if (user.getProfile() != null && user.getProfile().getTelephone().equals(answer)) {
            passtxt.setText(user.getPassword());
        } else {
            errorAnswer.setText("Incorrect phone number.");
        }
    }

}
