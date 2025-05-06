package edu.unidrive.controllers;
import edu.unidrive.entities.Trajet;
import edu.unidrive.services.TrajetService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class    DetailTrajetController implements Initializable {

    @FXML
    private TableView<Trajet> addedTripsTable;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<Trajet, Integer> idTrajetColumn;

    @FXML
    private TableColumn<Trajet, String> startPointColumn;

    @FXML
    private TableColumn<Trajet, String> destinationColumn;

    @FXML
    private TableColumn<Trajet, Integer> dureeColumn;

    @FXML
    private TableColumn<Trajet, Float> distanceColumn;

    @FXML
    private TableColumn<Trajet, Timestamp> dateColumn; // Format Timestamp -> String if needed

    @FXML
    private TableColumn<Trajet, Integer> seatsColumn;

    @FXML
    private Button editTripButton;

    @FXML
    private Button cancelTripButton;

    @FXML
    private AnchorPane manageAddedTripsPane;

    private  TrajetService trajetService = new TrajetService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up TableView columns
        idTrajetColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        startPointColumn.setCellValueFactory(new PropertyValueFactory<>("pointDepart"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("pointArrive"));
        dureeColumn.setCellValueFactory(new PropertyValueFactory<>("dureeEstimee"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("heureDepart"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("placeDisponible"));

        // Load data
        loadTrajets();

        cancelTripButton.setOnAction(e -> cancelSelectedTrip());
        backButton.setOnAction(e -> goBackToCreerTrajet());
        editTripButton.setOnAction(e -> modifySelectedTrip());

    }

    public void loadTrajets() {
        List<Trajet> trajets = trajetService.getAllData();

        if (trajets.isEmpty()) {
            System.out.println("⚠ No trajets found in the database.");
        } else {
            System.out.println("✅ Trajets loaded: " + trajets.size());
        }

        ObservableList<Trajet> currentList = addedTripsTable.getItems();
        currentList.setAll(trajets); // 🔥 Update the existing list instead of replacing it
        addedTripsTable.refresh();
    }


    private void goBackToCreerTrajet() {
        try {
            // Load the 'CreerTrajet.fxml' page
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreerTrajet.fxml"));
            Scene scene = new Scene(loader.load());
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cancelSelectedTrip() {
        Trajet selectedTrajet = addedTripsTable.getSelectionModel().getSelectedItem();
        if (selectedTrajet != null) {
            trajetService.deleteEntity(selectedTrajet); // Suppression dans la base de données
            loadTrajets(); // Mettre à jour la TableView
        } else {
            System.out.println("Aucun trajet sélectionné.");
        }
    }

    private void modifySelectedTrip() {
        Trajet selectedTrajet = addedTripsTable.getSelectionModel().getSelectedItem();
        if (selectedTrajet != null) {
            // Open Edit Trip Window
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTrajet.fxml"));
                AnchorPane editTripPane = loader.load();
                ModifierTrajetController editTripController = loader.getController();

                // Pre-fill the data from the selected trip
                editTripController.prefillFields(selectedTrajet);

                editTripController.setDetailTrajetController(this);

                // Create a new Stage for the Edit Trip Window
                Stage stage = new Stage();
                stage.setScene(new Scene(editTripPane));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠ No trip selected.");
        }
    }



    @FXML
    private void openModifierTrajetWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTrajet.fxml")); // Remplace avec le bon chemin
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre ModifierTrajet
            ModifierTrajetController modifierController = loader.getController();

            // Passer la référence du contrôleur actuel (DetailTrajetController)
            modifierController.setDetailTrajetController(this);

            // Ouvrir la fenêtre de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}