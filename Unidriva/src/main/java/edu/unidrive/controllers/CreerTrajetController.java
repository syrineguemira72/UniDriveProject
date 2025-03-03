    package edu.unidrive.controllers;

    import edu.unidrive.entities.Trajet;
    import edu.unidrive.services.OpenRouteServiceClient;
    import edu.unidrive.services.TrajetService;
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
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;
    import java.time.format.DateTimeParseException;
    import java.util.logging.Logger;

    public class CreerTrajetController {

        private static final Logger logger = Logger.getLogger(CreerTrajetController.class.getName());
        private static final String KM_SUFFIX = " km";
        private static final String MINUTES_SUFFIX = " minutes";

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
            // Ajouter des écouteurs pour les champs de départ et d'arrivée
            departField.textProperty().addListener((observable, oldValue, newValue) -> verifierEtCalculer());
            arriveeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) { // Si le champ perd le focus
                    verifierEtCalculer();
                }
            });    }

        private void verifierEtCalculer() {
            String depart = departField.getText().trim();
            String arrivee = arriveeField.getText().trim();

            // Vérifier que les deux champs sont remplis
            if (!depart.isEmpty() && !arrivee.isEmpty()) {
                calculerDistanceEtDuree();
            } else {
                // Effacer les champs si l'un des champs est vide
                distanceField.clear();
                dureeEstimeeField.clear();
            }
        }

        private void calculerDistanceEtDuree() {
            try {
                String depart = departField.getText();
                String arrivee = arriveeField.getText();

                // Récupérer les coordonnées et calculer la distance et la durée
                OpenRouteServiceClient client = new OpenRouteServiceClient();
                double[] departCoords = client.getCoordinates(depart);
                double[] arriveeCoords = client.getCoordinates(arrivee);
                double[] result = client.getDistanceAndDuration(departCoords[0], departCoords[1], arriveeCoords[0], arriveeCoords[1]);

                // Mettre à jour les champs distance et durée estimée
                distanceField.setText(String.format("%.2f%s", result[0], KM_SUFFIX));
                dureeEstimeeField.setText(String.format("%.2f%s", result[1], MINUTES_SUFFIX));
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
                String pointDepart = departField.getText();
                String pointArrive = arriveeField.getText();
                LocalDate date = dateDepartPicker.getValue();
                String heureText = heureField.getText();
                LocalTime heure = LocalTime.parse(heureText);
                LocalDateTime heureDepart = LocalDateTime.of(date, heure);
                int placesDisponibles = Integer.parseInt(placesDisponiblesField.getText());

                // Valider que tous les champs sont remplis
                if (pointDepart.isEmpty() || pointArrive.isEmpty() || date == null || heureText.isEmpty() || placesDisponiblesField.getText().isEmpty()) {
                    showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
                    return;
                }

                // Convertir la distance et la durée
                float distance = Float.parseFloat(distanceField.getText().replace(KM_SUFFIX, ""));
                int dureeEstimee = Integer.parseInt(dureeEstimeeField.getText().replace(MINUTES_SUFFIX, ""));

                // Créer et sauvegarder le trajet
                Trajet trajet = new Trajet();
                trajet.setPointDepart(pointDepart);
                trajet.setPointArrive(pointArrive);
                trajet.setHeureDepart(heureDepart);
                trajet.setDureeEstimee(dureeEstimee);
                trajet.setDistance(distance);
                trajet.setPlaceDisponible(placesDisponibles);

                TrajetService trajetService = new TrajetService();
                trajetService.addEntity(trajet);

                showAlert("Succès", "Trajet créé avec succès !", Alert.AlertType.INFORMATION);
                clearFields();
            } catch (DateTimeParseException e) {
                showAlert("Erreur", "Format d'heure invalide. Utilisez HH:mm.", Alert.AlertType.ERROR);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez saisir des valeurs valides.", Alert.AlertType.ERROR);
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

        private void showAlert(String titre, String message, Alert.AlertType type) {
            Alert alert = new Alert(type);
            alert.setTitle(titre);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }