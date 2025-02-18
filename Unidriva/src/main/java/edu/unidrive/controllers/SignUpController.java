package edu.unidrive.controllers;

import edu.unidrive.entities.Profile;
import edu.unidrive.entities.Utilisateur;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        // Vérification des champs vides
        if (email.isEmpty() || firstname.isEmpty() || lastname.isEmpty() ||
                pass.isEmpty() || gender == null || dobDate == null) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérification du format de l'email
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Email invalide", "Veuillez entrer un email valide.");
            return;
        }

        // Vérification si l'email est unique
        UserService userService = new UserService();
        if (!userService.isEmailUnique(email)) {
            showAlert(Alert.AlertType.ERROR, "Email déjà utilisé", "Cet email est déjà utilisé. Veuillez en choisir un autre.");
            return;
        }

        // Vérification du mot de passe
        if (!isValidPassword(pass)) {
            showAlert(Alert.AlertType.ERROR, "Mot de passe faible",
                    "Le mot de passe doit contenir au moins 8 caractères, une majuscule et un chiffre.");
            return;
        }

        // Vérification du genre
        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
            showAlert(Alert.AlertType.ERROR, "Genre invalide", "Veuillez choisir 'Male' ou 'Female'.");
            return;
        }

        // Convertir la date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dobString = dobDate.format(formatter);

        // Création de l'utilisateur et du profil
        Utilisateur user = new Utilisateur(email, dobString, gender, firstname, lastname, pass);
        Profile profile = new Profile("default_photo_url", "Bio par défaut", "0000000000", "Adresse par défaut");
        profile.setUtilisateur(user);
        user.setProfile(profile);

        ProfileService profileService = new ProfileService();

        try {
            userService.add(user);
            profileService.add(profile);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès !");

            // Redirection vers la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
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
