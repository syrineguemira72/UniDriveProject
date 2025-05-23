package edu.unidrive.controllers;

import edu.unidrive.entities.Etat;
import edu.unidrive.entities.Reservation;
import edu.unidrive.entities.Trajet;
import edu.unidrive.services.ReservationService;
import edu.unidrive.services.TrajetService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReserverTrajetController {

    @FXML
    private DatePicker datePickerRecherche;

    @FXML
    private VBox trajetsContainer; // VBox to hold trip cards

    @FXML
    private Button btnListeReservations;

    @FXML
    private Button btnRetour;

    private final TrajetService trajetService = new TrajetService();
    private final ReservationService reservationService = new ReservationService();

    private Trajet selectedTrajet; // Store the selected trip

    @FXML
    public void initialize() {
        loadTrajets(); // Load trips when the view is initialized
    }

    private void loadTrajets() {
        trajetsContainer.getChildren().clear(); // Clear existing cards

        List<Trajet> trajets = trajetService.getAllData();
        System.out.println("Nombre de trajets récupérés : " + trajets.size()); // Debug

        if (trajets.isEmpty()) {
            System.out.println("⚠ Aucun trajet trouvé.");
            return;
        }

        for (Trajet trajet : trajets) {
            try {
                // Load the trip card FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReserverCard.fxml"));
                AnchorPane tripCard = loader.load();

                // Get the controller for the trip card
                ReserverCardController cardController = loader.getController();
                cardController.setTrajet(trajet); // Set the trip data
                cardController.setReserverTrajetController(this); // Pass the parent controller

                // Store the controller in the card's properties
                tripCard.getProperties().put("controller", cardController);

                // Add the trip card to the VBox
                trajetsContainer.getChildren().add(tripCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to search trips by date
    @FXML
    private void rechercherTrajets() {
        LocalDate selectedDate = datePickerRecherche.getValue();
        if (selectedDate != null) {
            List<Trajet> trajets = trajetService.getTrajetsByDate(selectedDate);
            trajetsContainer.getChildren().clear(); // Clear existing cards

            for (Trajet trajet : trajets) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReserverCard.fxml"));
                    AnchorPane tripCard = loader.load();

                    ReserverCardController cardController = loader.getController();
                    cardController.setTrajet(trajet);
                    cardController.setReserverTrajetController(this);

                    trajetsContainer.getChildren().add(tripCard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            loadTrajets(); // Reload all trips if no date is selected
        }
    }

    // Method to handle trip reservation
    @FXML
    public void reserverTrajet(Trajet trajet) {
        if (trajet != null) {
            // Check if there are available seats
            if (trajet.getPlaceDisponible() > 0) {
                // Decrement the number of available seats
                trajet.setPlaceDisponible(trajet.getPlaceDisponible() - 1);

                // Update the trip in the database
                trajetService.updateEntity(trajet.getId(),trajet);

                Reservation newReservation = new Reservation(
                        0,
                        LocalDate.now(),// ID will be auto-generated
                        trajet,
                        Etat.pending  // Reservation date
                );
                
                reservationService.addEntity(newReservation);

                // Show a confirmation message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Réservation réussie");
                alert.setHeaderText("Votre réservation a été enregistrée avec succès");
                alert.setContentText("Vous avez réservé le trajet : " + trajet);
                alert.showAndWait();

                // Refresh the reservation card
                refreshReservationCard(trajet);
            } else {
                // Show a warning if no seats are available
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Plus de places disponibles");
                alert.setHeaderText("Aucune place disponible");
                alert.setContentText("Désolé, il n'y a plus de places disponibles pour ce trajet.");
                alert.showAndWait();
            }
        } else {
            // Show a warning if no trip is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de sélection");
            alert.setHeaderText("Aucun trajet sélectionné");
            alert.setContentText("Veuillez sélectionner un trajet avant de réserver.");
            alert.showAndWait();
        }
    }

    // Method to refresh the reservation card
    private void refreshReservationCard(Trajet trajet) {
        for (Node node : trajetsContainer.getChildren()) {
            if (node instanceof AnchorPane card) {
                ReserverCardController cardController = (ReserverCardController) card.getProperties().get("controller");
                if (cardController != null && cardController.getTrajet().equals(trajet)) {
                    cardController.refreshCard();
                    break;
                }
            }
        }
    }


    // Method to set the selected trip
    public void setSelectedTrajet(Trajet trajet) {
        this.selectedTrajet = trajet;
    }

    // Method to get the selected trip
    private Trajet getSelectedTrajet() {
        return selectedTrajet;
    }

    @FXML
    private void goToListeReservations() {
        try {
            // Load the ListeRéservation.fxml page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeReservations.fxml"));
            Stage stage = (Stage) btnListeReservations.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setHeaderText("Impossible de charger la page ListeRéservation.fxml");
            alert.setContentText("Une erreur est survenue lors du changement de page.");
            alert.showAndWait();
        }
    }

    @FXML
    private void goToCreerTrajet() {
        try {
            // Load the CreerTrajet.fxml page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeUnidrive.fxml"));
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setHeaderText("Impossible de charger la page CreerTrajet.fxml");
            alert.setContentText("Une erreur est survenue lors du changement de page.");
            alert.showAndWait();
        }
    }
}