package edu.unidrive.controllers;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

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
    private String generatedCode;
    private String userPhoneNumber;


    @FXML
    private Button backBtn;


    @FXML
    private Label errorLb;


    @FXML
    private TextField nametxt;

    @FXML
    private TextField passtxt;



    @FXML
    private TextField usernametxt;

    @FXML
    private TextField codeTxt;


    @FXML
    private Label codeErrorLb;

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

            nametxt.setText(user.getLastname() + " " + user.getFirstname());
            generatedCode = String.valueOf((int) (Math.random() * 9000) + 1000);
            userPhoneNumber = user.getProfile().getTelephone();

            sendSms(userPhoneNumber, "Your verification code is: " + generatedCode);
            codeErrorLb.setText("Verification code sent to your phone.");
        }
    }

    @FXML
    void RetrivePassword(ActionEvent event) {
        String code = codeTxt.getText().trim();
        String username = usernametxt.getText().trim();

        Utilisateur user = userService.getUserByEmail(username);

        if (user == null) {
            errorLb.setText("User not found!");
            return;
        }

        if (generatedCode != null && generatedCode.equals(code)) {
            passtxt.setText(user.getPassword());
            codeErrorLb.setText("");
        } else {
            codeErrorLb.setText("Incorrect verification code.");
        }
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            Twilio.init("ACbc66f19dfeb447b7cb2d0b5d88e58c62", "1818a9b9a44721d555bc5d79cebc324e");
            Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber("+18032192785"),
                    message
            ).create();
        } catch (Exception e) {
            e.printStackTrace();
            codeErrorLb.setText("Failed to send SMS.");
        }
    }
}
