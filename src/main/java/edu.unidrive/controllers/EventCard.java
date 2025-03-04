package edu.unidrive.controllers;

import edu.unidrive.entities.Event;
import edu.unidrive.services.EventService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class EventCard {

    @FXML
    private Label id_title;
    @FXML private Label id_description;
    @FXML private Label id_date;
    @FXML private Label id_activities;

    // Event data for the card
    private Event event;
    private EventService eventService = new EventService();
    private Pane parentContainer; // Reference to the parent container of this card


    // This method will populate the card with event data
    public void setEvent(Event event) {
        this.event = event;
        id_title.setText(event.getTitre());
        id_description.setText(event.getDescription());
        id_date.setText(event.getDate().toString());
        id_activities.setText(event.getActivities());  // Assuming activities is a String
    }

    @FXML
    private void updateEvent() {
        try {
            // Load the UpdateEvent FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cov/pidev3a8/hamza/UpdateEvent.fxml"));
            Parent root = loader.load();

            // Get the controller of the UpdateEvent window
            UpdateEvent updateEventController = loader.getController();

            // Pass the event to the update form
            updateEventController.setEventDetails(event);

            // Create a new stage for the update event window
            Stage stage = new Stage();
            stage.setTitle("Update Event");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "There was an error opening the update event window.");
        }
    }

    @FXML
    private void deleteEvent() {
        if (event != null) {
            // Show confirmation alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Event");
            alert.setHeaderText("Are you sure you want to delete this event?");
            alert.setContentText(event.getTitre());

            alert.showAndWait().ifPresent(response -> {
                if (response.getButtonData().isDefaultButton()) {
                    // Call service to delete event
                    eventService.deleteEvent(event.getId());

                    // Remove card from UI
                    if (parentContainer != null) {
                        parentContainer.getChildren().remove(id_title.getParent());
                    }

                    System.out.println("Deleted event: " + event.getTitre());
                }
            });
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    }

