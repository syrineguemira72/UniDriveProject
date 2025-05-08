package edu.unidrive.controllers;

import edu.unidrive.config.AppConfig;
import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.EmailService;
import edu.unidrive.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class SignUpController {

    @FXML
    private Button btnSave;
    @FXML
    private PasswordField password;
    @FXML
    private DatePicker txtDOB;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtFirstname;
    @FXML
    private ComboBox<String> txtGender;

    @FXML
    private TextField txtLastname;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private TextField txtStreet;
    @FXML
    private TextField txtGovernorate;
    @FXML
    private TextArea txtAboutMe;

    @FXML
    public void initialize() {
        ObservableList<String> genderOptions = FXCollections.observableArrayList("male", "female", "other");
        txtGender.setItems(genderOptions);


    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+?\\d{1,15}$");
    }

    @FXML
    void save(ActionEvent event) {
        String email = txtEmail.getText();
        String firstname = txtFirstname.getText();
        String lastname = txtLastname.getText();
        String gender = txtGender.getSelectionModel().getSelectedItem();
        String pass = password.getText();
        //String phoneNumber = txtPhoneNumber.getText();
        //String street = txtStreet.getText();
        //String governorate = txtGovernorate.getText();
        //String aboutMe = txtAboutMe.getText();
        LocalDate dobDate = txtDOB.getValue();
        //String selectedRole = txtRole.getSelectionModel().getSelectedItem();

        // Validation des champs obligatoires
        if (email.isEmpty() || firstname.isEmpty() || lastname.isEmpty() ||
                pass.isEmpty() || gender == null || dobDate == null ) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill in all required fields.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

       /* if (!phoneNumber.isEmpty() && !isValidPhoneNumber(phoneNumber)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Please enter a valid phone number (international format accepted).");
            return;
        }*/

        UserService userService = new UserService();
        if (!userService.isEmailUnique(email)) {
            showAlert(Alert.AlertType.ERROR, "Email Already Used", "This email is already taken. Please choose another one.");
            return;
        }

        if (!isValidPassword(pass)) {
            showAlert(Alert.AlertType.ERROR, "Weak Password",
                    "The password must be at least 8 characters long, contain an uppercase letter, and a number.");
            return;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate minimumDateOfBirth = currentDate.minusYears(18);

        if (dobDate.isAfter(minimumDateOfBirth)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Date of Birth", "You must be at least 18 years old to sign up.");
            return;
        }

        String plainPassword = password.getText(); // Ne pas utiliser trim() ici

        System.out.println("=== Password Hashing Debug ===");
        System.out.println("Plain password: '" + plainPassword + "'");

        Utilisateur user = new Utilisateur(email, lastname, firstname, plainPassword);
        userService.add(user);

        // Définition des propriétés supplémentaires
        user.setDob(dobDate);
        user.setGender(gender.toLowerCase());

        List<String> roles = new ArrayList<>();
        roles.add(Utilisateur.ROLE_USER); // ROLE_USER par défaut
        if (email.equals(AppConfig.ADMIN_EMAIL)) {
            roles.add(Utilisateur.ROLE_ADMIN);
        }
        user.setRoles(roles);

        try {
            // Sauvegarde de l'utilisateur



            // Mise à jour de l'URL de l'image dans l'utilisateur
            userService.update(user);

            // Envoi de l'email de bienvenue
            EmailService emailService = new EmailService("bhsryhab@gmail.com", "fpov oqjy nfdd gble");
            emailService.sendWelcomeEmail(email, firstname);

            showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully!");

            // Redirection vers la page de login
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}