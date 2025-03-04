package edu.unidrive.controllers;

import edu.unidrive.entities.Trajet;
import edu.unidrive.services.OpenRouteServiceClient;
import edu.unidrive.services.TrajetService;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

public class CreerTrajetController {

    private static final Logger logger = Logger.getLogger(CreerTrajetController.class.getName());
    private static final String KM_SUFFIX = " km";
    private static final String MINUTES_SUFFIX = " minutes";

    @FXML
    private ListView<String> departSuggestionsListView;

    @FXML
    private ListView<String> arriveeSuggestionsListView;

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
    public void initialize() {
        // Add a delay to avoid triggering the calculation too frequently
        PauseTransition pause = new PauseTransition(Duration.seconds(1)); // 1-second delay
        pause.setOnFinished(event -> verifierEtCalculer());

        // Add listeners to the departure and arrival fields
        departField.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.playFromStart(); // Restart the delay timer
            fetchAutocompleteSuggestions(newValue, departSuggestionsListView);
        });

        arriveeField.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.playFromStart(); // Restart the delay timer
            fetchAutocompleteSuggestions(newValue, arriveeSuggestionsListView);
        });

        // Add listeners to handle selection from the ListView
        departSuggestionsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                departField.setText(newValue);
                departSuggestionsListView.getItems().clear();
            }
        });

        arriveeSuggestionsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                arriveeField.setText(newValue);
                arriveeSuggestionsListView.getItems().clear();
            }
        });
    }

    private void fetchAutocompleteSuggestions(String input, ListView<String> suggestionsListView) {
        if (input.length() < 3) {
            suggestionsListView.getItems().clear(); // Clear suggestions if input is too short
            return;
        }

        new Thread(() -> {
            try {
                List<String> suggestions = new OpenRouteServiceClient().getAutocompleteSuggestions(input);
                Platform.runLater(() -> {
                    suggestionsListView.getItems().clear();
                    suggestionsListView.getItems().addAll(suggestions);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    showAlert("Erreur", "Impossible de récupérer les suggestions.", Alert.AlertType.ERROR);
                });
            }
        }).start();
    }

    private void verifierEtCalculer() {
        String depart = departField.getText().trim();
        String arrivee = arriveeField.getText().trim();

        // Check if both fields are filled
        if (!depart.isEmpty() && !arrivee.isEmpty()) {
            calculerDistanceEtDuree();
        } else {
            // Clear the fields if one of them is empty
            distanceField.clear();
            dureeEstimeeField.clear();
        }
    }

    private void calculerDistanceEtDuree() {
        try {
            String depart = departField.getText();
            String arrivee = arriveeField.getText();

            // Get coordinates from the API
            OpenRouteServiceClient client = new OpenRouteServiceClient();
            double[] departCoords = client.getCoordinates(depart);
            double[] arriveeCoords = client.getCoordinates(arrivee);

            // Check if the API recognized the locations
            if (departCoords == null) {
                showAlert("Erreur", "Impossible de trouver les coordonnées pour : " + depart, Alert.AlertType.ERROR);
                return;
            }

            if (arriveeCoords == null) {
                showAlert("Erreur", "Impossible de trouver les coordonnées pour : " + arrivee, Alert.AlertType.ERROR);
                return;
            }

            // Calculate distance and duration
            double[] result = client.getDistanceAndDuration(departCoords[0], departCoords[1], arriveeCoords[0], arriveeCoords[1]);

            // Update the distance and duration fields
            Platform.runLater(() -> {
                distanceField.setText(String.format("%.2f%s", result[0], KM_SUFFIX));
                dureeEstimeeField.setText(String.format("%.0f%s", result[1], MINUTES_SUFFIX));
            });
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de calculer la distance et la durée. Veuillez vérifier les noms des villes.", Alert.AlertType.ERROR);
            logger.severe("Erreur lors du calcul de la distance et de la durée : " + e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite. Veuillez réessayer.", Alert.AlertType.ERROR);
            logger.severe("Erreur inattendue : " + e.getMessage());
        }
    }

    @FXML
    void creerTrajet(ActionEvent event) {
        try {
            // Validate all fields are filled
            if (departField.getText().isEmpty() || arriveeField.getText().isEmpty() ||
                    dateDepartPicker.getValue() == null || heureField.getText().isEmpty() ||
                    placesDisponiblesField.getText().isEmpty() || distanceField.getText().isEmpty() ||
                    dureeEstimeeField.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
                return;
            }

            // Validate heureField format (HH:mm)
            String heureText = heureField.getText().trim();
            if (!heureText.matches("\\d{2}:\\d{2}")) {
                showAlert("Erreur", "Format d'heure invalide. Utilisez HH:mm.", Alert.AlertType.ERROR);
                return;
            }

            // Parse the time
            LocalTime heure = LocalTime.parse(heureText);

            // Parse distance and duration
            String distanceText = distanceField.getText().replace(KM_SUFFIX, "").trim(); // Remove suffix
            String dureeText = dureeEstimeeField.getText().replace(MINUTES_SUFFIX, "").trim(); // Remove suffix

            if (distanceText.isEmpty() || dureeText.isEmpty()) {
                showAlert("Erreur", "Veuillez calculer la distance et la durée avant de créer le trajet.", Alert.AlertType.ERROR);
                return;
            }

            // Parse distance as float
            float distance;
            try {
                distanceText = distanceText.replace(",", "."); // Handle commas as decimal separators
                distance = Float.parseFloat(distanceText);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "La distance doit être un nombre valide.", Alert.AlertType.ERROR);
                return;
            }

            // Parse dureeEstimee as integer
            int dureeEstimee;
            try {
                // Check if the text contains only numbers
                if (!dureeText.matches("\\d+")) {
                    showAlert("Erreur", "La durée estimée doit être un nombre entier valide.", Alert.AlertType.ERROR);
                    return;
                }
                dureeEstimee = Integer.parseInt(dureeText);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "La durée estimée doit être un nombre entier valide.", Alert.AlertType.ERROR);
                return;
            }

            // Create and save the Trajet object
            Trajet trajet = new Trajet();
            trajet.setPointDepart(departField.getText());
            trajet.setPointArrive(arriveeField.getText());
            trajet.setHeureDepart(LocalDateTime.of(dateDepartPicker.getValue(), heure));
            trajet.setDureeEstimee(dureeEstimee);
            trajet.setDistance(distance);
            trajet.setPlaceDisponible(Integer.parseInt(placesDisponiblesField.getText()));

            TrajetService trajetService = new TrajetService();
            trajetService.addEntity(trajet);

            showAlert("Succès", "Trajet créé avec succès !", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Format d'heure invalide. Utilisez HH:mm.", Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez saisir des valeurs valides pour la distance et la durée.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage(), Alert.AlertType.ERROR);
            logger.severe("Erreur lors de la création du trajet : " + e.getMessage());
        }
    }

    @FXML
    private void goToDetailTrajet(ActionEvent event) {
        navigateTo("/DetailTrajet.fxml", event);
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        navigateTo("/ReserverTrajet.fxml", event);
    }

    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            logger.severe("Erreur lors du chargement de " + fxmlFile + ": " + e.getMessage());
        }
    }

    private void clearFields() {
        arriveeField.clear();
        departField.clear();
        dateDepartPicker.setValue(null);
        distanceField.clear();
        dureeEstimeeField.clear();
        heureField.clear();
        placesDisponiblesField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}