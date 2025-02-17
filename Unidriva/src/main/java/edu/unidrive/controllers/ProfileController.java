package edu.unidrive.controllers;

import edu.unidrive.entities.Profile;
import edu.unidrive.services.ProfileService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Profile{

    @FXML
    private Button addbtn;

    @FXML
    private Button deletebtn;

    @FXML
    private TextField dob;

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

    @FXML
    void add(ActionEvent event) {
        Profile profile = new Profile("photo_url", "User bio", phone.getText(), email.getText());
        profileService.add(profile);
        System.out.println("Profile ajouté");
    }

    @FXML
    void delete(ActionEvent event) {
        Profile profile = new Profile();
        profile.setId(Integer.parseInt(username.getText())); // Suppose que l'ID est entré dans username
        profileService.remove(profile);
        System.out.println("Profile supprimé");
    }

    @FXML
    void edit(ActionEvent event) {
        Profile profile = new Profile(Integer.parseInt(username.getText()), "photo_url", "User bio", phone.getText(), email.getText());
        profileService.update(profile);
        System.out.println("Profile mis à jour");
    }

    @FXML
    void notifcation(MouseEvent event) {
        System.out.println("Notification clicked");
    }

    @FXML
    void password(MouseEvent event) {
        System.out.println("Password clicked");
    }
    
}