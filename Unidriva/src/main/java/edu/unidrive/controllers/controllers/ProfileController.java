package edu.unidrive.controllers.controllers;

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
            System.out.println("User found: " + utilisateur);

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

                dob.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        System.out.println("Date of Birth changed to: " + newValue);
                    }
                });
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
                System.out.println("No profiles found for user ID : " + utilisateur.getId());
            }
        } else {
            System.out.println("User not found!");
        }
    }



    @FXML
    void delete(ActionEvent event) {
        Utilisateur utilisateur = userService.getUserByEmail(currentUserEmail);
        if (utilisateur == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "User Not Found !");
            return;
        }

        Profile profile = utilisateur.getProfile();
        if (profile == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Profil Not Found!");
            return;
        }

        boolean deleted = false;

        if (phone.getText().isEmpty()) {
            System.out.println("Phone field is empty.");
            profile.setTelephone("");
            deleted = true;
        }

        if (email.getText().isEmpty()) {
            System.out.println("Email field is empty.");
            profile.setAdresse("");
            deleted = true;
        }

        if (username.getText().isEmpty()) {
            System.out.println("Username field is empty.");
            utilisateur.setLastname("");
            deleted = true;
        }

        if (dob.getValue() == null) {
            System.out.println("Date of Birth field is cleared.");
            utilisateur.setDob("");
            deleted = true;
        }

        if (!deleted) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun champ sélectionné pour suppression.");
            return;
        }

        profileService.update(profile);
        userService.update(utilisateur);
        showAlert(Alert.AlertType.INFORMATION, "Delete", "Field deleted successfully !");
    }

    @FXML
    void edit(ActionEvent event) {
        Utilisateur utilisateur = userService.getUserByEmail(currentUserEmail);
        if (utilisateur == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "User Not Found !");
            return;
        }

        Profile profile = utilisateur.getProfile();
        if (profile == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Profil Not Found !");
            return;
        }

        boolean updated = false;

        // Vérifier si le champ username a été modifié
        String newUsername = username.getText();
        String oldUsername = utilisateur.getFirstname() + " " + utilisateur.getLastname();
        if (!newUsername.equals(oldUsername)) {
            System.out.println("Username field is updated.");
            utilisateur.setLastname(newUsername);
            updated = true;
        }

        // Vérifier si le champ email a été modifié
        String newEmail = email.getText();
        String oldEmail = utilisateur.getEmail();
        if (!newEmail.equals(oldEmail)) {
            System.out.println("Email field is updated.");
            utilisateur.setEmail(newEmail);
            updated = true;
        }

        // Vérifier si le champ phone a été modifié
        String newPhone = phone.getText();
        String oldPhone = profile.getTelephone();
        if (!newPhone.equals(oldPhone)) {
            System.out.println("Phone field is updated.");
            profile.setTelephone(newPhone);
            updated = true;
        }

        // Vérifier si le champ dob a été modifié
        LocalDate newDob = dob.getValue();
        String oldDobString = utilisateur.getDob();
        if (newDob != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String newDobString = newDob.format(formatter);
            if (!newDobString.equals(oldDobString)) {
                System.out.println("Date of Birth field is updated.");
                utilisateur.setDob(newDobString);
                updated = true;
            }
        }

        if (!updated) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "No fields selected for editing.");
            return;
        }

        profileService.update(profile);
        userService.update(utilisateur);

        showAlert(Alert.AlertType.INFORMATION, "Mise à jour", "The selected field has been updated successfully!");
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