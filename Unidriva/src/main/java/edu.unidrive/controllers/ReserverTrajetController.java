package edu.unidrive.controllers;

import edu.unidrive.entities.Etat;
import edu.unidrive.entities.Reservation;
import edu.unidrive.entities.Trajet;
import edu.unidrive.services.ReservationService;
import edu.unidrive.services.TrajetService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReserverTrajetController {

    @FXML
    private TableColumn<?, ?> colArrivee;

    @FXML
    private TableColumn<?, ?> colDepart;

    @FXML
    private TableColumn<?, ?> colHeure;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colPlaces;

    @FXML
    private DatePicker datePickerRecherche;

    @FXML
    private TableView<Trajet> tableTrajets;

    @FXML
    private Button btnListeReservations;

    @FXML
    private Button btnRetour;

    private final TrajetService trajetService = new TrajetService();

    private final ReservationService reservationService = new ReservationService();



    @FXML
    public void initialize() {
        // Associer les colonnes aux attributs de la classe Trajet
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDepart.setCellValueFactory(new PropertyValueFactory<>("pointDepart"));
        colArrivee.setCellValueFactory(new PropertyValueFactory<>("pointArrive"));
        colHeure.setCellValueFactory(new PropertyValueFactory<>("heureDepart"));
        colPlaces.setCellValueFactory(new PropertyValueFactory<>("placeDisponible"));

        // Charger tous les trajets au démarrage
        chargerTousLesTrajets();
    }

    private void chargerTousLesTrajets() {
        List<Trajet> trajets = trajetService.getAllData();
        ObservableList<Trajet> trajetsObservable = FXCollections.observableArrayList(trajets);
        tableTrajets.setItems(trajetsObservable);
    }

    @FXML
    private void reserverTrajet(ActionEvent event) {
        // Récupérer le trajet sélectionné dans la table
        Trajet selectedTrajet = tableTrajets.getSelectionModel().getSelectedItem();

        if (selectedTrajet != null) {
            // Créer une nouvelle réservation
            Reservation newReservation = new Reservation(
                    0,  // ID sera généré automatiquement par la base de données (si vous utilisez une clé auto-incrémentée)
                    selectedTrajet,
                    Etat.En_attente,  // L'état initial de la réservation (par exemple "En attente")
                    LocalDate.now()   // La date de réservation est la date actuelle
            );


            reservationService.addEntity(newReservation);

            // Afficher un message de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Réservation réussie");
            alert.setHeaderText("Votre réservation a été enregistrée avec succès");
            alert.setContentText("Vous avez réservé le trajet : " + selectedTrajet);
            alert.showAndWait();

        } else {
            // Alerte si aucun trajet n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de sélection");
            alert.setHeaderText("Aucun trajet sélectionné");
            alert.setContentText("Veuillez sélectionner un trajet avant de réserver.");
            alert.showAndWait();
        }
    }
    @FXML
    private void goToListeReservations(ActionEvent event) {
        try {
            // Charger la page ListeRéservation.fxml
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
    private void goToCreerTrajet(ActionEvent event) {
        try {
            // Charger la page CreerTrajet.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreerTrajet.fxml"));
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