package edu.unidrive.controllers;

import edu.unidrive.entities.Event;
import edu.unidrive.services.ActivityService;
import edu.unidrive.services.EventService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateEvent {

    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker datePicker;
    @FXML private ComboBox<String> activityComboBox;


    private EventService eventService;
    private Event currentEvent;
    private final ActivityService activityService = new ActivityService();

    public UpdateEvent() {
        eventService = new EventService();
    }
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
    // Set the details of the event to be updated
    public void setEventDetails(Event event) {
        this.currentEvent = event;
        titreField.setText(event.getTitre());
        descriptionField.setText(event.getDescription());

        // Convert the java.sql.Date to LocalDate and set it to DatePicker
        LocalDate localDate = datePicker.getValue(); // Get the selected date from DatePicker

    }

    // Update the event in the database
    @FXML
    public void updateEvent() {
        String titre = titreField.getText();
        String description = descriptionField.getText();
        LocalDate localDate = datePicker.getValue();
        String selectedActivity = activityComboBox.getValue();

        if (localDate != null && !titre.isEmpty() && !description.isEmpty()) {
            // Convert LocalDate back to java.sql.Date
            Date date = java.sql.Date.valueOf(localDate);
            currentEvent.setTitre(titre);
            currentEvent.setDescription(description);
            currentEvent.setDate(date);
            currentEvent.setActivities(selectedActivity
            );

            // Call service to update the event in the database
            eventService.updateEvent(currentEvent);


            closeWindow();
            Notifications notifications= Notifications.create();
            notifications.text("Event Details Updated");
            notifications.title("Return to Main Menu ");
            notifications.hideAfter(Duration.seconds(5));
            notifications.show();
        }

    }


    // Close the window after updating the event
    private void closeWindow() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }
}
