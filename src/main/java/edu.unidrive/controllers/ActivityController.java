package edu.unidrive.controllers;

import edu.unidrive.entities.Activity;
import edu.unidrive.services.ActivityService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class ActivityController {

    @FXML
    private TableView<Activity> activityTable;
    @FXML
    private TableColumn<Activity, String> titreColumn;
    @FXML
    private TableColumn<Activity, String> descriptionColumn;
    @FXML
    private TableColumn<Activity, LocalDate> dateColumn;
    @FXML
    private TableColumn<Activity, Button> actionColumn;  // Column for delete button
    @FXML
    private TableColumn<Activity, Button> updateColumn;  // Column for update button

    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker datePicker;

    private ActivityService activityService;
    private Activity selectedActivity; // Holds the selected activity for update

    public ActivityController() {
        activityService = new ActivityService();
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Set up the Action column with Delete button
        actionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Activity, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Activity, Button> param) {
                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> deleteActivity(param.getValue()));  // Delete activity
                return new SimpleObjectProperty<>(deleteButton);
            }
        });

        // Set up the Update column with Update button
        updateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Activity, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Activity, Button> param) {
                Button updateButton = new Button("Update");
                updateButton.setOnAction(event -> updateActivity(param.getValue()));  // Update activity
                return new SimpleObjectProperty<>(updateButton);
            }
        });

        // Load all activities into the table
        loadActivities();
    }

    private void loadActivities() {
        ObservableList<Activity> activities = FXCollections.observableArrayList(activityService.getAllActivities());
        activityTable.setItems(activities);
    }

    @FXML
    public void addActivity() {
        String titre = titreField.getText();
        String description = descriptionField.getText();
        LocalDate localDate = datePicker.getValue(); // Get the selected date from DatePicker

        if (localDate != null && !titre.isEmpty() && !description.isEmpty()) {
            // Convert LocalDate to Date
            Date date = java.sql.Date.valueOf(localDate);  // Convert to java.sql.Date for database storage
            Activity activity = new Activity(0, titre, description, date);
            activityService.addActivity(activity);
            loadActivities();
            clearFields();
        }
    }

    private void clearFields() {
        titreField.clear();
        descriptionField.clear();
        datePicker.setValue(null); // Clear the DatePicker
    }

    // Method to update the selected activity
    private void updateActivity(Activity activity) {
        // Store the selected activity
        selectedActivity = activity;

        try {
            // Load the UpdateUser.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cov/pidev3a8/hamza/UpdateActivityPage.fxml"));
            Parent root = loader.load();


            // Access the controller and pass user data to it
            UpdateActivityController Controller  = loader.getController();
            Controller.setActivityDetails(selectedActivity);  // Set selected activity details

            // Create a new stage for the popup window
            Stage stage = new Stage();
            stage.setTitle("Update User");
            stage.setScene(new Scene(root));

            // Set modality to APPLICATION_MODAL to make the popup window modal
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup window
            stage.showAndWait();

            // After the popup window is closed, refresh the TableView if needed
            // For example, if the update operation modifies the user object in a way visible to the TableView
            // you can refresh the table here
            // table.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }
        refreshTable();

    }
    private void refreshTable() {
        // Fetch the updated list of activities (you can call a method in ActivityService to fetch data)
        ObservableList<Activity> updatedActivities = FXCollections.observableArrayList(activityService.getAllActivities());

        // Update the table with the new data
        activityTable.setItems(updatedActivities);
    }

    // Method to delete an activity
    private void deleteActivity(Activity activity) {
        if (activity != null) {
            // Delete the activity from the service (database)
            activityService.deleteActivity(activity.getId());
            loadActivities(); // Reload the activities after deletion
            showAlert("Activity Deleted", "The activity was successfully deleted.");
        } else {
            showAlert("Error", "No activity selected for deletion.");
        }
    }

    // Method to show an alert for success or error
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void NavigateToActivity(ActionEvent actionEvent) {
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
}
