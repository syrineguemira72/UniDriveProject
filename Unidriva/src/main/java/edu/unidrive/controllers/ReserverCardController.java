package edu.unidrive.controllers;

import edu.unidrive.entities.Trajet;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ReserverCardController {

    @FXML
    private Label departLabel;

    @FXML
    private Label arriveeLabel;

    @FXML
    private Label dureeLabel;

    @FXML
    private Label heureLabel;

    @FXML
    private Label placesLabel;

    @FXML
    private Button reserveButton;

    private Trajet trajet;
    private ReserverTrajetController reserverTrajetController;

    // Set the trip data
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        updateUI();
    }

    // Set the parent controller
    public void setReserverTrajetController(ReserverTrajetController reserverTrajetController) {
        this.reserverTrajetController = reserverTrajetController;
    }

    // Update the UI with trip data
    private void updateUI() {
        departLabel.setText("Point de départ: " + trajet.getPointDepart());
        arriveeLabel.setText("Destination: " + trajet.getPointArrive());
        dureeLabel.setText("Temps estimé: " + trajet.getDureeEstimee() + " minutes");
        heureLabel.setText("Heure de départ: " + trajet.getHeureDepart().toLocalTime().toString());
        placesLabel.setText("Places disponibles: " + trajet.getPlaceDisponible());

        // Add event handler for the reserve button
        reserveButton.setOnAction(event -> {
            if (trajet != null && reserverTrajetController != null) {
                reserverTrajetController.reserverTrajet(trajet);
            }
        });
    }

    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}