package edu.unidrive.controllers;

import edu.unidrive.entities.Trajet;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TrajetCardController {

    @FXML
    private Label departLabel;

    @FXML
    private Label arriveeLabel;

    @FXML
    private Label dureeLabel;

    @FXML
    private Label heureLabel;

    @FXML
    private Button modifierButton;

    @FXML
    private Button annulerButton;

    private Trajet trajet;
    private DetailTrajetController detailTrajetController;

    // Set the trip data
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        updateUI();
    }

    // Set the parent controller
    public void setDetailTrajetController(DetailTrajetController detailTrajetController) {
        this.detailTrajetController = detailTrajetController;
    }

    // Update the UI with trip data
    private void updateUI() {
        departLabel.setText("Point de départ: " + trajet.getPointDepart());
        arriveeLabel.setText("Destination: " + trajet.getPointArrive());
        dureeLabel.setText("Temps estimé: " + trajet.getDureeEstimee() + " minutes");
        heureLabel.setText("Heure de départ: " + trajet.getHeureDepart().toLocalTime().toString());

        // Add event handlers for the buttons
        modifierButton.setOnAction(event -> detailTrajetController.openModifierTrajetWindow(trajet));
        annulerButton.setOnAction(event -> {
            try {
                detailTrajetController.deleteTrip(trajet);
            } catch (RuntimeException e) {
                // Show an alert to the user
                showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}