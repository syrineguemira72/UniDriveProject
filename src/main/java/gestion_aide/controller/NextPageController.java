package gestion_aide.controller;

import gestion_aide.entities.aide;
import gestion_aide.services.AideService;
import gestion_aide.entities.AideType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class NextPageController{

    @FXML
    private TextField typetextfield;

    @FXML
    private TextField descriptiontextfield;

    @FXML
    private TextField montanttextfield;

    @FXML
    private TableView<aide> aideTable;  // TableView to display aides

    @FXML
    private TableColumn<aide, Integer> idColumn;  // Column for ID
    @FXML
    private TableColumn<aide, String> typeColumn;  // Column for Nom
    @FXML
    private TableColumn<aide, String> descriptionColumn;  // Column for Prenom
    @FXML
    private TableColumn<aide, String> montantColumn;

    private AideService aideService;  // Service class for interacting with the database

    public NextPageController() {
        aideService = new AideService();
    }

    // Method to initialize the TableView with data
    @FXML
    private void initialize() {
        // Set up the columns with PropertyValueFactory
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));

        // Add listener to handle row selection
        aideTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Populate the TextFields with the selected row data
                typetextfield.setText(newValue.getType());
                descriptiontextfield.setText(newValue.getDescription());
                montanttextfield.setText(newValue.getMontant());
            }
        });

        // Load the data into the TableView
        loadData();
    }

    // Method to load data from the database and bind to the TableView
    private void loadData() {
        List<aide> aides = aideService.getallData();  // Get all data from the database
        ObservableList<aide> aideList = FXCollections.observableArrayList(aides);  // Convert list to ObservableList
        aideTable.setItems(aideList);  // Set the data in the TableView
    }

    @FXML
    void ajouterAideaction(ActionEvent event) {
        String type = typetextfield.getText();
        String description = descriptiontextfield.getText();
        String montant = montanttextfield.getText();

        // Check if any field is empty
        if (type.isEmpty() || description.isEmpty() || montant.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        // Validate that the type is one of the predefined values
        if (!AideType.isValidType(type)) {
            showAlert("Erreur", "Le type doit être 'alimentaire', 'financier', ou 'médical'.", Alert.AlertType.ERROR);
            return;
        }

        // Validate that the montant is a valid number
        try {
            Double montantValue = Double.parseDouble(montant);
            if (montantValue <= 0) {
                showAlert("Erreur", "Le montant doit être un nombre positif.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.", Alert.AlertType.ERROR);
            return;
        }

        // Create the aide object and add it using the service
        aide aide = new aide(type, description, montant);
        AideService aideService = new AideService();
        aideService.addEntity(aide);

        // Show success alert
        showAlert("Succès", "L'aide a été ajoutée avec succès.", Alert.AlertType.INFORMATION);

        // Load the detail view
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Detail.fxml"));
        try {
            Parent root = fxmlLoader.load();
            DetailController detailController = fxmlLoader.getController();
            detailController.setTypetextfield(type);
            detailController.setDescriptiontextfield(description);
            detailController.setMontanttextfield(montant);
            typetextfield.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur ajout: " + e.getMessage());
        }
    }
    @FXML
    void deleteSelectedRow(ActionEvent event) {
        // Get the selected aide from the table
        aide selectedAide = aideTable.getSelectionModel().getSelectedItem();

        if (selectedAide != null) {
            // Confirm with the user before deleting
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to delete this aide?");
            alert.setContentText("Type: " + selectedAide.getType() + "\nDescription: " + selectedAide.getDescription());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Call the deleteEntity method to remove the aide from the database
                AideService aideService = new AideService();
                aideService.deleteEntity(selectedAide.getId(), selectedAide);

                // Remove the item from the TableView after deletion
                aideTable.getItems().remove(selectedAide);

                // Show success alert
                showAlert("Succès", "L'aide a été supprimée avec succès.", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une aide à supprimer.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void updateAction(ActionEvent event) {
        // Get the selected row from the TableView
        aide selectedAide = aideTable.getSelectionModel().getSelectedItem();

        if (selectedAide == null) {
            showAlert("Erreur", "Aucune ligne sélectionnée pour la mise à jour.", Alert.AlertType.ERROR);
            return;
        }

        // Get the updated values from the TextFields
        String updatedType = typetextfield.getText();
        String updatedDescription = descriptiontextfield.getText();
        String updatedMontant = montanttextfield.getText();

        // Validate the inputs before updating
        if (updatedType.isEmpty() || updatedDescription.isEmpty() || updatedMontant.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        // Create a new aide object with the updated values
        aide updatedAide = new aide(updatedType, updatedDescription, updatedMontant);

        // Call the service method to update the entity in the database
        AideService aideService = new AideService();
        aideService.updateEntity(selectedAide.getId(), updatedAide);

        // Refresh the TableView after updating
        loadData();

        // Show success message
        showAlert("Succès", "Aide mise à jour avec succès.", Alert.AlertType.INFORMATION);
    }
    @FXML
    void goToAnotherPage(ActionEvent event) {
        // Load the new FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NextPage.fxml"));
        try {
            // Load the new page and set it as the root
            Parent root = fxmlLoader.load();
            // Set the new scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur de navigation : " + e.getMessage());
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void goToBack(ActionEvent event) {
        // Load the new FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CRUD.fxml"));
        try {
            // Load the new page and set it as the root
            Parent root = fxmlLoader.load();
            // Set the new scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur de navigation : " + e.getMessage());
        }
    }

}


