package edu.unidrive.controllers;

import edu.unidrive.entities.aide;
import edu.unidrive.services.AideService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AideAdmin {

    @FXML
    private TextField searchField;
    @FXML
    private TextField currencytextfield;
    @FXML
    private TextField descriptiontextfield;
    @FXML
    private TextField montanttextfield;

    @FXML
    private TableView<aide> aideTable;

    @FXML
    private TableColumn<aide, Integer> idColumn;
    @FXML
    private TableColumn<aide, String> currencyColumn;
    @FXML
    private TableColumn<aide, String> descriptionColumn;
    @FXML
    private TableColumn<aide, String> montantColumn;
    @FXML
    private TableColumn<aide, String> createdAtColumn;

    private AideService aideService;

    public AideAdmin() {
        aideService = new AideService();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        currencyColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        loadData();

        // Wrap data in a FilteredList
        FilteredList<aide> filteredData = new FilteredList<>(aideTable.getItems(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(aide -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return aide.getCurrency().toLowerCase().contains(lowerCaseFilter)
                        || aide.getDescription().toLowerCase().contains(lowerCaseFilter)
                        || aide.getMontant().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<aide> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(aideTable.comparatorProperty());
        aideTable.setItems(sortedData);
    }

    private void loadData() {
        List<aide> aides = aideService.getallData();
        ObservableList<aide> aideList = FXCollections.observableArrayList(aides);
        aideTable.setItems(aideList);
    }

    @FXML
    void ajouterAideaction(ActionEvent event) {
        String currency = currencytextfield.getText();
        String description = descriptiontextfield.getText();
        String montant = montanttextfield.getText();

        if (currency.isEmpty() || description.isEmpty() || montant.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.INFORMATION);
            return;
        }

        // Validate montant
        try {
            Double montantValue = Double.parseDouble(montant);
            if (montantValue <= 0) {
                showAlert("Erreur", "Le montant doit être un nombre positif.", Alert.AlertType.INFORMATION);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.", Alert.AlertType.INFORMATION);
            return;
        }
    }

    @FXML
    void deleteSelectedRow(ActionEvent event) {
        aide selectedAide = aideTable.getSelectionModel().getSelectedItem();

        if (selectedAide != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette aide ?");
            alert.setContentText("Currency: " + selectedAide.getCurrency() + "\nDescription: " + selectedAide.getDescription());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                aideService.deleteEntity(selectedAide.getId(), selectedAide);

                // Reload the TableView with updated data
                loadData();

                showAlert("Succès", "L'aide a été supprimée avec succès.", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une aide à supprimer.", Alert.AlertType.ERROR);
        }
    }




    @FXML
    void updateAction(ActionEvent event) {
        aide selectedAide = aideTable.getSelectionModel().getSelectedItem();

        if (selectedAide == null) {
            showAlert("Erreur", "Aucune ligne sélectionnée pour la mise à jour.", Alert.AlertType.ERROR);
            return;
        }

        String updatedCurrency = currencytextfield.getText().trim();
        String updatedDescription = descriptiontextfield.getText().trim();
        String updatedMontant = montanttextfield.getText().trim();

        if (updatedCurrency.isEmpty() || updatedDescription.isEmpty() || updatedMontant.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        try {
            double montant = Double.parseDouble(updatedMontant);
            if (montant <= 0) {
                showAlert("Erreur", "Le montant doit être un nombre positif.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.", Alert.AlertType.ERROR);
            return;
        }

        // Update the aide with the new values
        aide updatedAide = new aide(updatedCurrency, updatedDescription, updatedMontant);
        aideService.updateEntity(selectedAide.getId(), updatedAide);

        loadData();
        showAlert("Succès", "Aide mise à jour avec succès.", Alert.AlertType.INFORMATION);
    }
    @FXML
    void goToAnotherPage(ActionEvent event) {
        // Load the new FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BeneficiaireAdminPage.fxml"));
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
    // Helper method to show alerts
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
