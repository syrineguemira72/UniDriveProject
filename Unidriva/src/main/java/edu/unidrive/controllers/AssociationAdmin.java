package edu.unidrive.controllers;

import edu.unidrive.entities.Association;
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

    @FXML private TextField nomTextField;
    @FXML private TextField adresseTextField;
    @FXML private TextField telephoneTextField;
    @FXML private TextField emailTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private TextField imageTextField;

    @FXML private TextField searchField;

    @FXML private TableView<Association> associationTable;
    @FXML private TableColumn<Association, Integer> idColumn;
    @FXML private TableColumn<Association, String> nomColumn;
    @FXML private TableColumn<Association, String> adresseColumn;
    @FXML private TableColumn<Association, String> telephoneColumn;
    @FXML private TableColumn<Association, String> emailColumn;
    @FXML private TableColumn<Association, String> descriptionColumn;
    @FXML private TableColumn<Association, String> imageColumn;

    private AssociationService associationService;

    public AssociationAdmin() {
        this.associationService = new AssociationService();
    }

    @FXML
    private void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        loadData();

        // Setup search
        FilteredList<Association> filteredData = new FilteredList<>(associationTable.getItems(), p -> true);
        searchField.textProperty().addListener((obs, old, val) -> {
            String filter = (val == null) ? "" : val.toLowerCase();
            filteredData.setPredicate(assoc -> {
                if (filter.isEmpty()) return true;
                return assoc.getNom().toLowerCase().contains(filter)
                        || assoc.getAdresse().toLowerCase().contains(filter)
                        || assoc.getTelephone().toLowerCase().contains(filter)
                        || assoc.getEmail().toLowerCase().contains(filter)
                        || String.valueOf(assoc.getId()).contains(filter);
            });
        });
        SortedList<Association> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(associationTable.comparatorProperty());
        associationTable.setItems(sortedData);

        associationTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) populateFields(selected);
        });
    }

    private void populateFields(Association assoc) {
        nomTextField.setText(assoc.getNom());
        adresseTextField.setText(assoc.getAdresse());
        telephoneTextField.setText(assoc.getTelephone());
        emailTextField.setText(assoc.getEmail());
        descriptionTextArea.setText(assoc.getDescription());
        imageTextField.setText(assoc.getImage());
    }

    private void loadData() {
        List<Association> list = associationService.getallData();
        ObservableList<Association> obsList = FXCollections.observableArrayList(list);
        associationTable.setItems(obsList);
    }

    @FXML
    void ajouterAssociationAction(ActionEvent event) {
        String nom = nomTextField.getText();
        String adresse = adresseTextField.getText();
        String telephone = telephoneTextField.getText();
        String email = emailTextField.getText();
        String description = descriptionTextArea.getText();
        String image = imageTextField.getText();

        if (nom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Tous les champs obligatoires doivent être remplis.", Alert.AlertType.ERROR);
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

        Association assoc = new Association(nom, adresse, telephone, email, description, image);
        associationService.addEntity(assoc);
        showAlert("Succès", "Association ajoutée.", Alert.AlertType.INFORMATION);
        loadData();
        clearFields();
    }

    @FXML
    void deleteSelectedRow(ActionEvent event) {
        Association selected = associationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une association.", Alert.AlertType.ERROR);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cette association?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.YES) {
            associationService.deleteEntity(selected.getId(), selected);
            showAlert("Succès", "Association supprimée.", Alert.AlertType.INFORMATION);
            loadData();
        }
    }

    @FXML
    void updateAction(ActionEvent event) {
        Association selected = associationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une association.", Alert.AlertType.ERROR);
            return;
        }
        String nom = nomTextField.getText();
        String adresse = adresseTextField.getText();
        String telephone = telephoneTextField.getText();
        String email = emailTextField.getText();
        String description = descriptionTextArea.getText();
        String image = imageTextField.getText();

        if (nom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Tous les champs obligatoires doivent être remplis.", Alert.AlertType.ERROR);
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

        selected.setNom(nom);
        selected.setAdresse(adresse);
        selected.setTelephone(telephone);
        selected.setEmail(email);
        selected.setDescription(description);
        selected.setImage(image);

        associationService.updateEntity(selected.getId(), selected);
        showAlert("Succès", "Association mise à jour.", Alert.AlertType.INFORMATION);
        loadData();
    }

    @FXML
    void goToBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AideAdminPage.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Navigation error: " + e.getMessage());
        }
    }

    private void clearFields() {
        nomTextField.clear();
        adresseTextField.clear();
        telephoneTextField.clear();
        emailTextField.clear();
        descriptionTextArea.clear();
        imageTextField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
