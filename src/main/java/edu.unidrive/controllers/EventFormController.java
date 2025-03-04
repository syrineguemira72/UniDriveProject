package edu.unidrive.controllers;

import javax.mail.Authenticator;

import edu.unidrive.entities.Event;
import edu.unidrive.services.ActivityService;
import edu.unidrive.services.EventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import java.io.IOException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.mail.Session;

import javafx.scene.control.TextField;

public class EventFormController {
    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> activityComboBox;

    private final EventService eventService = new EventService();
    private final ActivityService activityService = new ActivityService();
    private static final String FROM_EMAIL = "malekrabaaoui12@gmail.com";
    private static final String FROM_PASSWORD = "gldu dajx gzvd ygbv";
    private static final String EMAIL_SUBJECT = "Password Reset";
    @FXML
    public void initialize() {
        loadActivitiesFromDatabase();
    }

    private void loadActivitiesFromDatabase() {
        // Get the activities from the database and populate the ComboBox
        List<String> activities = activityService.getAllActivities().stream()
                .map(activity -> activity.getTitre()) // Assuming Activity has a getTitre() method
                .collect(Collectors.toList());
        activityComboBox.getItems().addAll(activities); // Add activity titles to ComboBox

        // Set a default selection if there are activities
        if (!activities.isEmpty()) {
            activityComboBox.setValue(activities.get(0));
        }
    }



    @FXML
    private void handleAddEvent() {
        // Get the values from the form fields
        String titre = titreField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate localDate = datePicker.getValue();
        String selectedActivity = activityComboBox.getValue();

        // Validate the inputs
        if (titre.isEmpty() || !titre.matches("[a-zA-Z ]+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Title", "Title must contain only letters.");
            return;
        }
        if (description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Description", "Description cannot be empty.");
            return;
        }
        if (localDate == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Date", "Please select a valid date.");
            return;
        }
        if (selectedActivity == null || selectedActivity.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "No Activity", "Please select an activity.");
            return;
        }

        // Show the pop-up window for verification
        try {

            // Set the generated code in the pop-up controller


           // This method will handle the verification logic

            // If verification is successful, add the event

                // Create the event with the selected activity
                Event event = new Event(0, titre, description, Date.valueOf(localDate), selectedActivity);

                // Add the event
                eventService.addEvent(event);

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Event added successfully!");
            sendEmail("jlassihamza769@gmail.com", "");

                // Reset form
                titreField.clear();
                descriptionField.clear();
                datePicker.setValue(null);
                activityComboBox.setValue(null);
        } catch (Exception e) {
            System.err.println("Error adding event: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error adding event: " + e.getMessage());
        }
    }

    @FXML
    private void goToEventList(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cov/pidev3a8/hamza/ShowEvent.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void NavigateToEvent(ActionEvent actionEvent) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/cov/pidev3a8/hamza/EventForm.fxml"));

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private void sendEmail(String toEmail, String newPassword) {
        // Set email properties
        String titre = titreField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate localDate = datePicker.getValue();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Authenticate with SMTP server using javax.mail.Authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                // Pass the password as a String instead of char[]
                return new javax.mail.PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD); // FROM_PASSWORD should be a String
            }
        };

        // Create session
        Session session = Session.getInstance(props, auth);

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(FROM_EMAIL));

            // Set To: header field
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            // Set Subject: header field
            message.setSubject("Event Creation");
            String emailContent = "Here are the event details:\n\n" +
                    "Title: " + titre + "\n" +
                    "Description: " + description + "\n";
            message.setText(emailContent);

            // Send email
            Transport.send(message);

            System.out.println("Password reset email sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Failed to send password reset email. Error: " + e.getMessage());
        }

    }

    public void NavigateToActtivity(ActionEvent actionEvent) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/cov/pidev3a8/hamza/activity_view.fxml"));

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}


