package edu.unidrive.controllers;

import edu.unidrive.entities.Profile;
import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.ProfileService;
import edu.unidrive.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class ProfileController {

    @FXML
    private Button addbtn;

    @FXML
    private Label backbtn;

    @FXML
    private Button deletebtn;

    @FXML
    private DatePicker dob;

    @FXML
    private Button editbtn;

    @FXML
    private TextField email;

    @FXML
    private Label name;

    @FXML
    private Label notificationbtn;

    @FXML
    private Label passwordbtn;

    @FXML
    private TextField phone;

    @FXML
    private Label profilebtn;

    @FXML
    private TextField username;

    private ProfileService profileService = new ProfileService();
    private UserService userService = new UserService();

    private String currentUserEmail;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        initializeUserData(email);
    }

    public void initializeUserData(String email) {
        Utilisateur utilisateur = userService.getUserByEmail(email);

        if (utilisateur != null) {
            System.out.println("Utilisateur trouvé : " + utilisateur);

            if (username != null) {
                username.setText(utilisateur.getFirstname() + " " + utilisateur.getLastname());
            }
            if (name != null) {
                name.setText(utilisateur.getFirstname() + " " + utilisateur.getLastname());
            }
            if (dob != null) {
                String dobString = utilisateur.getDob();
                if (dobString != null && !dobString.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dobDate = LocalDate.parse(dobString, formatter);
                    dob.setValue(dobDate);
                }
            }
            if (this.email != null) {
                this.email.setText(utilisateur.getEmail());
            }

            Profile profile = utilisateur.getProfile();
            if (profile != null) {
                if (phone != null) {
                    phone.setText(profile.getTelephone());
                }
            } else {
                System.out.println("Aucun profil trouvé pour l'utilisateur ID : " + utilisateur.getId());
            }
        } else {
            System.out.println("Utilisateur non trouvé !");
        }
    }

    @FXML
    void add(ActionEvent event) {
        System.out.println("Current User Email: " + currentUserEmail); // Log pour vérifier l'email

        Utilisateur utilisateur = userService.getUserByEmail(currentUserEmail);


        String photo = "photo par defaut ";
        String bio = "Bio par défaut";
        String telephone = phone.getText();
        String adresse = email.getText();


        if (utilisateur == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé !");
            return;
        }

        Profile profile = new Profile(photo, bio, telephone, adresse);
        profile.setUtilisateur(utilisateur);

        profileService.add(profile);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil ajouté avec succès !");
    }

    @FXML
    void delete(ActionEvent event) {
        phone.requestFocus();

        Utilisateur utilisateur = userService.getUserByEmail(currentUserEmail);
        if (utilisateur == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé !");
            return;
        }

        Profile profile = utilisateur.getProfile();
        if (profile == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Profil non trouvé !");
            return;
        }

        if (phone.isFocused()) {
            System.out.println("Phone field is focused.");
            profile.setTelephone("");
            phone.clear();
        } else if (email.isFocused()) {
            System.out.println("Email field is focused.");
            profile.setAdresse("");
            email.clear();
        } else if (username.isFocused()) {
            System.out.println("Username field is focused.");
            utilisateur.setLastname("");
            username.clear();
        } else {
            System.out.println("No field is focused.");
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun champ sélectionné pour suppression.");
            return;
        }

        profileService.update(profile);
        userService.update(utilisateur);
        showAlert(Alert.AlertType.INFORMATION, "Suppression", "Champ supprimé avec succès !");
    }

    @FXML
    void edit(ActionEvent event) {
        Utilisateur utilisateur = userService.getUserByEmail(currentUserEmail);
        if (utilisateur == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé !");
            return;
        }

        Profile profile = utilisateur.getProfile();
        if (profile == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Profil non trouvé !");
            return;
        }

        profile.setPhoto("photo_url");
        profile.setBio("User bio");
        profile.setTelephone(phone.getText());
        profile.setAdresse(email.getText());

        utilisateur.setLastname(username.getText());
        utilisateur.setEmail(email.getText());

        profileService.update(profile);
        userService.update(utilisateur);

        showAlert(Alert.AlertType.INFORMATION, "Mise à jour", "Profil et utilisateur mis à jour avec succès !");
    }


    @FXML
    void back(MouseEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("HomeUniDrive.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) backbtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void notifcation(MouseEvent event) {
    }

    @FXML
    void password(MouseEvent event) {
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}