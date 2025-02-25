package cov.pidev3A8.controllers;

import cov.pidev3A8.entities.Trajet;
import cov.pidev3A8.services.TrajetService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreerTrajetController {

    @FXML
    private TextField arriveeField;

    @FXML
    private TextField departField;

    @FXML
    private TextField distanceField;

    @FXML
    private TextField heureField;

    @FXML
    private TextField dureeEstimeeField;


    @FXML
    private DatePicker dateDepartPicker;

    @FXML
    private TextField placesDisponiblesField;

    @FXML
    void creerTrajet(ActionEvent event) {
        try {
            // Récupérer les valeurs des champs de saisie
            String pointDepart = departField.getText();
            String pointArrive = arriveeField.getText();
            LocalDate date = dateDepartPicker.getValue();
            String heureText = heureField.getText(); // Récupérer l'heure au format "HH:mm"
            LocalTime heure = LocalTime.parse(heureText);
            LocalDateTime heureDepart = LocalDateTime.of(date, heure); // Combiner date et heure
            int dureeEstimee = Integer.parseInt(dureeEstimeeField.getText());
            float distance = Float.parseFloat(distanceField.getText());
            int placesDisponibles = Integer.parseInt(placesDisponiblesField.getText());

            // Vérifier que tous les champs sont remplis
            if (pointDepart.isEmpty() || pointArrive.isEmpty() || date == null || heureField.getText().isEmpty() || dureeEstimeeField.getText().isEmpty() || distanceField.getText().isEmpty() || placesDisponiblesField.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
                return;
            }

            // Créer un nouvel objet Trajet
            Trajet trajet = new Trajet();
            trajet.setPointDepart(pointDepart);
            trajet.setPointArrive(pointArrive);
            trajet.setHeureDepart(heureDepart);
            trajet.setDureeEstimee(dureeEstimee);
            trajet.setDistance(distance);
            trajet.setPlaceDisponible(placesDisponibles);

            // Ajouter le trajet via le service
            TrajetService trajetService = new TrajetService();
            trajetService.addEntity(trajet);

            // Afficher un message de succès
            showAlert("Succès", "Trajet créé avec succès !", Alert.AlertType.INFORMATION);

            // Rediriger vers la page detailTrajet
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DetailTrajet.fxml"));
            Parent root = fxmlLoader.load();

            // Récupérer le contrôleur et charger les données
            DetailTrajetController controller = fxmlLoader.getController();
            controller.loadTrajets();

            // Réinitialiser les champs de saisie
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez saisir des valeurs numériques valides pour la durée, la distance et les places.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la création du trajet : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void goToDetailTrajet(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailTrajet.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de DetailTrajet.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReserverTrajet.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ReserverTrajet.fxml: " + e.getMessage());
        }
    }

    private void clearFields() {
        arriveeField.clear();
        departField.clear();
        dateDepartPicker.setValue(null);
        distanceField.clear();
        dureeEstimeeField.clear();
        placesDisponiblesField.clear();
    }

    private void showAlert(String erreur, String s, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

}