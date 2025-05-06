package edu.unidrive.controllers;

import edu.unidrive.entities.Association;
import edu.unidrive.entities.aide;
import edu.unidrive.services.AideService;
import edu.unidrive.services.AssociationService;
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

public class AssociationAdmin {

    @FXML
    private TextField nomtextfield, adressetextfield, telephonetextfield, emailtextfield;

    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox<aide> aideChoiceBox;

    @FXML
    private TableView<Association> associationTable;

    @FXML
    private TableColumn<Association, Integer> idColumn;
    @FXML
    private TableColumn<Association, String> nomColumn, adresseColumn, telephoneColumn, emailColumn;

    private AssociationService associationService;
    private AideService aideService;

    public AssociationAdmin() {
        associationService = new AssociationService();
        aideService = new AideService();
    }

    @FXML
    private void initialize() {
        // Set cell value factories for table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadData();  // Load data into the table
        loadAideData();  // Load data for choice box

        // Create a filtered list for searching
        FilteredList<Association> filteredData = new FilteredList<>(associationTable.getItems(), p -> true);

        // Add listener for search field text changes
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(association -> {
                // If the search field is empty, return all associations
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                // Check if the fields contain the text input by the user (case-insensitive)
                return association.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        association.getAdresse().toLowerCase().contains(lowerCaseFilter) ||
                        association.getTelephone().toLowerCase().contains(lowerCaseFilter) ||
                        association.getEmail().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(association.getId()).contains(lowerCaseFilter) || // Check against the ID
                        (association.getAideId() != null && String.valueOf(association.getAideId()).contains(lowerCaseFilter)); // Check against aideId
            });
        });

        // Create a SortedList to allow sorting in the TableView
        SortedList<Association> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(associationTable.comparatorProperty());
        associationTable.setItems(sortedData);

        // Add listener to populate fields when an association is selected from the table
        associationTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue);  // Populate fields with selected association data
            }
        });
    }



    private void populateFields(Association association) {
        nomtextfield.setText(association.getNom());
        adressetextfield.setText(association.getAdresse());
        telephonetextfield.setText(association.getTelephone());
        emailtextfield.setText(association.getEmail());

        for (aide a : aideChoiceBox.getItems()) {
            if (a.getId() == association.getAideId()) {
                aideChoiceBox.setValue(a);
                break;
            }
        }
    }

    private void loadData() {
        List<Association> associations = associationService.getallData();
        ObservableList<Association> associationList = FXCollections.observableArrayList(associations);
        associationTable.setItems(associationList);
    }

    private void loadAideData() {
        List<aide> aides = aideService.getallData();
        ObservableList<aide> aideList = FXCollections.observableArrayList(aides);
        aideChoiceBox.setItems(aideList);
        aideChoiceBox.setConverter(new javafx.util.StringConverter<aide>() {
            @Override
            public String toString(aide aide) {
                return aide != null ? String.valueOf(aide.getId()) : "";
            }
            @Override
            public aide fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    void ajouterAssociationAction(ActionEvent event) {
        String nom = nomtextfield.getText();
        String adresse = adressetextfield.getText();
        String telephone = telephonetextfield.getText();
        String email = emailtextfield.getText();
        aide selectedAide = aideChoiceBox.getValue();
        int aideId = selectedAide != null ? selectedAide.getId() : -1;

        if (nom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        if (!telephone.matches("\\d{8}")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres.", Alert.AlertType.ERROR);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Erreur", "L'email doit être valide.", Alert.AlertType.ERROR);
            return;
        }

        Association association = new Association(nom, adresse, telephone, email, aideId);
        associationService.addEntity(association);
        showAlert("Succès", "L'association a été ajoutée.", Alert.AlertType.INFORMATION);
        loadData();
    }

    @FXML
    void deleteSelectedRow(ActionEvent event) {
        Association selectedAssociation = associationTable.getSelectionModel().getSelectedItem();
        if (selectedAssociation != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous supprimer cette association?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                associationService.deleteEntity(selectedAssociation.getId(), selectedAssociation);
                loadData();
                showAlert("Succès", "Association supprimée.", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une association à supprimer.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    void updateAction(ActionEvent event) {
        Association selectedAssociation = associationTable.getSelectionModel().getSelectedItem();
        if (selectedAssociation == null) {
            showAlert("Erreur", "Veuillez sélectionner une association.", Alert.AlertType.ERROR);
            return;
        }

        String nom = nomtextfield.getText();
        String adresse = adressetextfield.getText();
        String telephone = telephonetextfield.getText();
        String email = emailtextfield.getText();

        // Validation des champs obligatoires
        if (nom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        // Validation du numéro de téléphone (8 chiffres)
        if (!telephone.matches("\\d{8}")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres.", Alert.AlertType.ERROR);
            return;
        }

        // Validation du format de l'email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Erreur", "L'email doit être dans un format valide (ex: exemple@domaine.com).", Alert.AlertType.ERROR);
            return;
        }

            // Mise à jour du Association
            selectedAssociation.setNom(nom);
            selectedAssociation.setAdresse(adresse);
            selectedAssociation.setTelephone(telephone);
            selectedAssociation.setEmail(email);

            // Mise à jour de l'aide sélectionnée
            aide selectedAide = aideChoiceBox.getValue();
            if (selectedAide != null) {
                selectedAssociation.setAideId(selectedAide.getId());
            } else {
                selectedAssociation.setAideId(null);
            }

            // Mise à jour dans la base de données
            associationService.updateEntity(selectedAssociation.getId(), selectedAssociation);
            showAlert("Succès", "Association mis à jour avec succès.", Alert.AlertType.INFORMATION);
            loadData();

    }
    @FXML
    void goToBack(ActionEvent event) {
        // Load the new FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AideAdminPage.fxml"));
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
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}