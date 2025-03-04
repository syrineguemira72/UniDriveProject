package edu.unidrive.controllers;

import edu.unidrive.entities.Activity;
import edu.unidrive.services.ActivityService;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Date;

public class UpdateActivityController {

    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker datePicker;

    private ActivityService activityService;
    private Activity currentActivity;

    public UpdateActivityController() {
        activityService = new ActivityService();
    }

    public void setActivityDetails(Activity activity) {
        this.currentActivity = activity;
        titreField.setText(activity.getTitre());
        descriptionField.setText(activity.getDescription());
        LocalDate localDate = datePicker.getValue(); // Get the selected date from DatePicker
    }

    @FXML
    public void updateActivity() {
        String titre = titreField.getText();
        String description = descriptionField.getText();
        LocalDate localDate = datePicker.getValue();

        if (localDate != null && !titre.isEmpty() && !description.isEmpty()) {
            Date date = java.sql.Date.valueOf(localDate);
            currentActivity.setTitre(titre);
            currentActivity.setDescription(description);
            currentActivity.setDate(date);

            activityService.updateActivity(currentActivity);
            closeWindow();
        }
    }

    private void closeWindow() {
        // Close the update window
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }
}
