package edu.unidrive.controllers;

import edu.unidrive.entities.Trajet;
import edu.unidrive.services.TrajetService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ModifierTrajetController {

    @FXML
    private TextField dateField;

    @FXML
    private TextField destinationField;

    @FXML
    private TextField distanceField;

    @FXML
    private TextField dureeField;

    @FXML
    private AnchorPane editTripPane;

    @FXML
    private Button saveButton;

    @FXML
    private TextField seatsField;

    @FXML
    private TextField startPointField;

    private TrajetService trajetService = new TrajetService(); // Assuming your service has update functionality

    private Trajet selectedTrajet; // To store the selected trip for editing

    private DetailTrajetController detailTrajetController;

    public void setDetailTrajetController(DetailTrajetController controller) {
        this.detailTrajetController = controller;
    }

    // Method to prefill fields with the selected trip's data
    public void prefillFields(Trajet trajet) {
        selectedTrajet = trajet; // Store the selected trip
        startPointField.setText(trajet.getPointDepart());
        destinationField.setText(trajet.getPointArrive());
        dureeField.setText(String.valueOf(trajet.getDureeEstimee()));
        distanceField.setText(String.valueOf(trajet.getDistance()));
        seatsField.setText(String.valueOf(trajet.getPlaceDisponible()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Manually extract the Timestamp and convert to LocalDateTime
        Timestamp timestamp = Timestamp.valueOf(trajet.getHeureDepart());
        if (timestamp != null) {
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            String formattedDate = localDateTime.format(formatter); // Formatting LocalDateTime
            dateField.setText(formattedDate);
        } else {
            // Handle the case where HeureDepart is null
            dateField.setText("No date available");
        }
    }


    @FXML
    public void saveEditedTrip() {
        // Gather updated data from the fields
        String startPoint = startPointField.getText();
        String destination = destinationField.getText();
        int duree = Integer.parseInt(dureeField.getText());
        float distance = Float.parseFloat(distanceField.getText());
        int seats = Integer.parseInt(seatsField.getText());
        String dateStr = dateField.getText();

        // Parse the Date string to Timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
        Timestamp dateHeure = Timestamp.valueOf(localDateTime);

        // Update the selected trip with the new data
        selectedTrajet.setPointDepart(startPoint);
        selectedTrajet.setPointArrive(destination);
        selectedTrajet.setDureeEstimee(duree);
        selectedTrajet.setDistance(distance);
        selectedTrajet.setPlaceDisponible(seats);
        selectedTrajet.setHeureDepart(dateHeure.toLocalDateTime());

        // Update the trip in the database
        trajetService.updateEntity(selectedTrajet.getId(), selectedTrajet);

        if (detailTrajetController != null) {
            detailTrajetController.loadTrajets(); // Reload data
        }

        // Close the Edit Trip Window
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

        // Optionally, you can refresh the table in the main window here if needed
        // For example, calling a method like: loadTrajets();
    }


}