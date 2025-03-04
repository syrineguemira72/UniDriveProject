package edu.unidrive.controllers;

import edu.unidrive.config.AppConfig;
import edu.unidrive.entities.Profile;
import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.EmailService;
import edu.unidrive.services.ProfileService;
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
import java.time.format.DateTimeFormatter;
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
    public void initialize() {
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Female", "Male");
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

    @FXML
    void save(ActionEvent event) {
        String email = txtEmail.getText();
        String firstname = txtFirstname.getText();
        String lastname = txtLastname.getText();
        String gender = txtGender.getSelectionModel().getSelectedItem();
        String pass = password.getText();
        LocalDate dobDate = txtDOB.getValue();

        if (email.isEmpty() || firstname.isEmpty() || lastname.isEmpty() ||
                pass.isEmpty() || gender == null || dobDate == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill in all the fields.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

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

        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Gender", "Please select 'Male' or 'Female'.");
            return;
        }

        // Vérification de l'âge
        LocalDate currentDate = LocalDate.now();
        LocalDate minimumDateOfBirth = currentDate.minusYears(18);

        if (dobDate.isAfter(minimumDateOfBirth)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Date of Birth", "You must be at least 18 years old to sign up.");
            return;
        }

        // Hacher le mot de passe avec BCrypt
        String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dobString = dobDate.format(formatter);

        // Chemin de l'image par défaut
        String defaultPhotoPath = "/images/profile.jpg";

        // Définir le rôle en fonction de l'e-mail
        String role = email.equals(AppConfig.ADMIN_EMAIL) ? "ADMIN" : "USER";

        // Utiliser le mot de passe haché pour créer l'utilisateur
        Utilisateur user = new Utilisateur(email, dobString, gender, firstname, lastname, hashedPassword, role); // Utilisez la variable 'role'

        // Ajouter l'utilisateur à la base de données
        try {
            userService.add(user); // L'ID de l'utilisateur est maintenant défini après l'insertion

            // Créer le profil avec l'ID de l'utilisateur
            Profile profile = new Profile(defaultPhotoPath, "Default bio", "00000000", email);
            profile.setUtilisateur(user); // Associer le profil à l'utilisateur

            ProfileService profileService = new ProfileService();
            profileService.add(profile);

            showAlert(Alert.AlertType.INFORMATION, "Success", "User and profile successfully registered!");

            // Envoyer un e-mail de bienvenue
            EmailService emailService = new EmailService("bhsryhab@gmail.com", "fpov oqjy nfdd gble");
            emailService.sendWelcomeEmail(email, firstname);

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
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