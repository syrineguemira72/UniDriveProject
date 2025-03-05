package edu.unidrive.controllers;

import edu.unidrive.entities.Profile;
import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.ProfileService;
import edu.unidrive.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
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

    @FXML
    private ImageView profileImage;

    @FXML
    private Button uploadPhotoBtn;

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
            }
            if (this.email != null) {
                this.email.setText(utilisateur.getEmail());
            }

            Profile profile = utilisateur.getProfile();
            if (profile != null) {
                if (phone != null) {
                    phone.setText(profile.getTelephone());
                }
                if (profileImage != null && profile.getPhoto() != null) {
                    // Vérifier si le fichier existe avant de le charger
                    File photoFile = new File(profile.getPhoto());
                    if (photoFile.exists()) {
                        try {
                            Image image = new Image(new FileInputStream(photoFile));
                            profileImage.setImage(image);
                        } catch (IOException e) {
                            System.err.println("Failed to load profile photo: " + e.getMessage());
                            profileImage.setImage(new Image(getClass().getResource("/images/profile.png").toString()));
                        }
                    } else {
                        System.err.println("Profile photo file not found: " + profile.getPhoto());
                    }
                }
            } else {
                System.out.println("No profiles found for user ID : " + utilisateur.getId());
            }
        } else {
            System.out.println("User not found!");
        }
    }

    @FXML
    void uploadPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(uploadPhotoBtn.getScene().getWindow());
        if (selectedFile != null) {
            try {
                byte[] photoBytes = Files.readAllBytes(selectedFile.toPath());

                Utilisateur utilisateur = userService.getUserByEmail(currentUserEmail);
                if (utilisateur != null) {
                    Profile profile = utilisateur.getProfile();
                    if (profile != null) {
                        profile.setPhoto(selectedFile.getAbsolutePath()); // Enregistrer le chemin de l'image
                        profileService.update(profile);

                        Image image = new Image(selectedFile.toURI().toString());
                        profileImage.setImage(image);

                        showAlert(Alert.AlertType.INFORMATION, "Success", "Profile photo updated successfully!");

                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("HomeUniDrive.fxml"));
                        Parent root = loader.load();
                        HomeUniDriveController homeController = loader.getController();
                        homeController.setProfileImage(selectedFile.toURI().toString()); // Transmettre l'URL de l'image

                        Stage stage = (Stage) backbtn.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                }
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload photo: " + e.getMessage());
            }
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

        String newEmail = email.getText();
        String oldEmail = utilisateur.getEmail();
        if (!newEmail.equals(oldEmail)) {
            System.out.println("Email field is updated.");
            utilisateur.setEmail(newEmail);
            updated = true;
        }

        String newPhone = phone.getText();
        String oldPhone = profile.getTelephone();
        if (!newPhone.equals(oldPhone)) {
            System.out.println("Phone field is updated.");
            profile.setTelephone(newPhone);
            updated = true;
        }

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GetPassword.fxml"));
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
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}