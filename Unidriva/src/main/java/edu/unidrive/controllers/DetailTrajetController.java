package edu.unidrive.controllers;

import edu.unidrive.entities.Trajet;
import edu.unidrive.services.TrajetService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DetailTrajetController implements Initializable {

    @FXML
    private VBox tripsContainer; // VBox to hold trip cards

    @FXML
    private Button backButton;

    private TrajetService trajetService = new TrajetService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTrajets(); // Load trips when the view is initialized
        backButton.setOnAction(e -> goBackToCreerTrajet());
    }

    // Method to load trips dynamically
    public void loadTrajets() {
        tripsContainer.getChildren().clear(); // Clear existing cards

        List<Trajet> trajets = trajetService.getAllData();
        if (trajets.isEmpty()) {
            System.out.println("⚠ Aucun trajet trouvé.");
            return;
        }

        for (Trajet trajet : trajets) {
            try {
                // Load the trip card FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/TrajetCard.fxml"));
                AnchorPane tripCard = loader.load();

                // Get the controller for the trip card
                TrajetCardController cardController = loader.getController();
                cardController.setTrajet(trajet); // Set the trip data
                cardController.setDetailTrajetController(this); // Pass the parent controller

                // Add the trip card to the VBox
                tripsContainer.getChildren().add(tripCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to delete a trip
    @FXML
    public void deleteTrip(Trajet trajet) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce trajet ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                trajetService.deleteEntity(trajet);
                loadTrajets();
            } catch (RuntimeException e) {
                showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    // Method to open the modifier window
    public void openModifierTrajetWindow(Trajet trajet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTrajet.fxml"));
            VBox  editTripPane = loader.load();
            ModifierTrajetController editTripController = loader.getController();

            // Pre-fill the fields with the selected trip's data
            editTripController.prefillFields(trajet);
            editTripController.setDetailTrajetController(this);

            // Open the modifier window
            Stage stage = new Stage();
            stage.setScene(new Scene(editTripPane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to go back to the CreerTrajet view
    private void goBackToCreerTrajet() {
        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreerTrajet.fxml"));
            Scene scene = new Scene(loader.load());
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}