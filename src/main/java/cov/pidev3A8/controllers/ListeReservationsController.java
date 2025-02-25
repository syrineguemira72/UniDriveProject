package cov.pidev3A8.controllers;

import cov.pidev3A8.entities.Reservation;
import cov.pidev3A8.entities.Etat;
import cov.pidev3A8.services.ReservationService;
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
import java.util.List;
import java.util.Optional;

public class ListeReservationsController {

    @FXML
    private TableView<Reservation> tableReservations;

    @FXML
    private TableColumn<Reservation, Integer> colId;

    @FXML
    private TableColumn<Reservation, String> colTrajet;

    @FXML
    private TableColumn<Reservation, Etat> colEtat;

    @FXML
    private TableColumn<Reservation, String> colDateReservation;

    @FXML
    private Button btnRetour;

    private final ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        // Initialiser les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTrajet.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(() ->
                cellData.getValue().getTrajet().getPointDepart() + " -> " + cellData.getValue().getTrajet().getPointArrive()
        ));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colDateReservation.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));

        // Définir un cellFactory pour la colonne Etat pour afficher un ComboBox
        colEtat.setCellFactory(param -> new TableCell<Reservation, Etat>() {
            private ComboBox<Etat> comboBox;

            @Override
            protected void updateItem(Etat item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (comboBox == null) {
                        comboBox = new ComboBox<>();
                        comboBox.setItems(FXCollections.observableArrayList(Etat.values()));
                        comboBox.setValue(item);
                        comboBox.setOnAction(e -> {
                            Reservation currentReservation = getTableView().getItems().get(getIndex());
                            currentReservation.setEtat(comboBox.getValue());
                            reservationService.updateEtatReservation(currentReservation.getId(), comboBox.getValue());
                            // Refresh the selected row after updating
                            tableReservations.refresh();
                        });
                    }
                    setGraphic(comboBox);
                    setText(null);
                }
            }
        });

        // Charger les données
        loadReservations();
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationService.getAllData();
        ObservableList<Reservation> observableList = FXCollections.observableArrayList(reservations);
        tableReservations.setItems(observableList);
    }

    @FXML
    private void retourPageReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReserverTrajet.fxml"));
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveEtat() {
        for (Reservation res : tableReservations.getItems()) {
            System.out.println("ID: " + res.getId() + ", État: " + res.getEtat());
        }

        // Refresh the table to reflect the saved states
        tableReservations.refresh();
    }

    @FXML
    private void supprimerReservation(ActionEvent event) {
        Reservation selectedReservation = tableReservations.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            showAlert("Erreur", "Veuillez sélectionner une réservation à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression de la réservation");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ReservationService reservationService = new ReservationService();
            reservationService.deleteEntity(selectedReservation);

            // Rafraîchir la table après suppression
            tableReservations.getItems().remove(selectedReservation);

            showAlert("Succès", "Réservation supprimée avec succès.", Alert.AlertType.INFORMATION);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}