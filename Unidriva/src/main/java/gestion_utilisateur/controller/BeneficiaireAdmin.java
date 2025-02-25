package gestion_utilisateur.controller;

import gestion_utilisateur.entities.Beneficiaire;
import gestion_utilisateur.entities.aide;
import gestion_utilisateur.services.BeneficiaireService;
import gestion_utilisateur.services.AideService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class BeneficiaireAdmin {

    @FXML
    private TextField nomtextfield, prenomtextfield, agetextfield, adressetextfield, telephonetextfield, emailtextfield;

    @FXML
    private ChoiceBox<aide> aideChoiceBox;

    @FXML
    private TableView<Beneficiaire> beneficiaireTable;

    @FXML
    private TableColumn<Beneficiaire, Integer> idColumn;
    @FXML
    private TableColumn<Beneficiaire, String> nomColumn, prenomColumn,adresseColumn, telephoneColumn, emailColumn;
    @FXML
    private TableColumn<Beneficiaire, Integer> ageColumn;

    private BeneficiaireService beneficiaireService;
    private AideService aideService;

    public BeneficiaireAdmin() {
        beneficiaireService = new BeneficiaireService();
        aideService = new AideService();
    }

    @FXML
    private void initialize() {
        // Initialize TableView columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Load initial data
        loadData();
        loadAideData();

        // Add listener for row selection in TableView
        beneficiaireTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue);
            }
        });
    }

    // Method to populate the fields when a row is selected
    private void populateFields(Beneficiaire beneficiaire) {
        nomtextfield.setText(beneficiaire.getNom());
        prenomtextfield.setText(beneficiaire.getPrenom());
        agetextfield.setText(String.valueOf(beneficiaire.getAge()));
        adressetextfield.setText(beneficiaire.getAdresse());
        telephonetextfield.setText(beneficiaire.getTelephone());
        emailtextfield.setText(beneficiaire.getEmail());

        // Find and select the corresponding aide in ChoiceBox
        for (aide a : aideChoiceBox.getItems()) {
            if (a.getId() == beneficiaire.getAideId()) {
                aideChoiceBox.setValue(a);
                break;
            }
        }
    }


    private void loadData() {
        List<Beneficiaire> beneficiaires = beneficiaireService.getallData();
        ObservableList<Beneficiaire> beneficiaireList = FXCollections.observableArrayList(beneficiaires);
        beneficiaireTable.setItems(beneficiaireList);
    }

    private void loadAideData() {
        List<aide> aides = aideService.getallData();
        ObservableList<aide> aideList = FXCollections.observableArrayList(aides);

        // Set the items for the ChoiceBox
        aideChoiceBox.setItems(aideList);

        // Use setConverter to display only the ID of the aide
        aideChoiceBox.setConverter(new javafx.util.StringConverter<aide>() {
            @Override
            public String toString(aide aide) {
                return aide != null ? String.valueOf(aide.getId()) : "";
            }

            @Override
            public aide fromString(String string) {
                // We don't need this method for our use case.
                return null;
            }
        });
    }

    @FXML
    void ajouterBeneficiaireAction(ActionEvent event) {
        String nom = nomtextfield.getText();
        String prenom = prenomtextfield.getText();
        String ageStr = agetextfield.getText();
        String adresse = adressetextfield.getText();
        String telephone = telephonetextfield.getText();
        String email = emailtextfield.getText();

        // Get the selected aide from the choice box
        aide selectedAide = aideChoiceBox.getValue();
        int aideId = selectedAide != null ? selectedAide.getId() : -1; // Default to -1 if no aide is selected

        // Validate that all fields are filled
        if (nom.isEmpty() || prenom.isEmpty() || ageStr.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        // Validate the telephone number (must be 8 digits)
        if (!telephone.matches("\\d{8}")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres.", Alert.AlertType.ERROR);
            return;
        }

        // Validate the email format (basic check for '@' and domain)
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Erreur", "L'email doit être dans un format valide (ex: exemple@domaine.com).", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Validate the age (must be a valid integer)
            int age = Integer.parseInt(ageStr);

            // Check if age is between 18 and 99
            if (age < 18 || age > 99) {
                showAlert("Erreur", "L'âge doit être compris entre 18 et 99 ans.", Alert.AlertType.ERROR);
                return;
            }

            // Create Beneficiaire with aideId
            Beneficiaire beneficiaire = new Beneficiaire(nom, prenom, age, adresse, telephone, email, aideId);
            beneficiaireService.addEntity(beneficiaire);
            showAlert("Succès", "Le bénéficiaire a été ajouté avec succès.", Alert.AlertType.INFORMATION);
            loadData();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'âge doit être un nombre valide.", Alert.AlertType.ERROR);
        }
    }



    @FXML
    void deleteSelectedRow(ActionEvent event) {
        Beneficiaire selectedBeneficiaire = beneficiaireTable.getSelectionModel().getSelectedItem();
        if (selectedBeneficiaire != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Voulez-vous supprimer ce bénéficiaire?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                beneficiaireService.deleteEntity(selectedBeneficiaire.getId(),selectedBeneficiaire);
                beneficiaireTable.getItems().remove(selectedBeneficiaire);
                showAlert("Succès", "Bénéficiaire supprimé.", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un bénéficiaire à supprimer.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void updateAction(ActionEvent event) {
        Beneficiaire selectedBeneficiaire = beneficiaireTable.getSelectionModel().getSelectedItem();
        if (selectedBeneficiaire == null) {
            showAlert("Erreur", "Veuillez sélectionner un bénéficiaire.", Alert.AlertType.ERROR);
            return;
        }

        String nom = nomtextfield.getText();
        String prenom = prenomtextfield.getText();
        String ageStr = agetextfield.getText();
        String adresse = adressetextfield.getText();
        String telephone = telephonetextfield.getText();
        String email = emailtextfield.getText();

        // Validation des champs obligatoires
        if (nom.isEmpty() || prenom.isEmpty() || ageStr.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
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

        try {
            // Validation et conversion de l'âge
            int age = Integer.parseInt(ageStr);
            if (age < 18 || age > 99) {
                showAlert("Erreur", "L'âge doit être compris entre 18 et 99 ans.", Alert.AlertType.ERROR);
                return;
            }

            // Mise à jour du bénéficiaire
            selectedBeneficiaire.setNom(nom);
            selectedBeneficiaire.setPrenom(prenom);
            selectedBeneficiaire.setAge(age);
            selectedBeneficiaire.setAdresse(adresse);
            selectedBeneficiaire.setTelephone(telephone);
            selectedBeneficiaire.setEmail(email);

            // Mise à jour de l'aide sélectionnée
            aide selectedAide = aideChoiceBox.getValue();
            if (selectedAide != null) {
                selectedBeneficiaire.setAideId(selectedAide.getId());
            } else {
                selectedBeneficiaire.setAideId(null);
            }

            // Mise à jour dans la base de données
            beneficiaireService.updateEntity(selectedBeneficiaire.getId(), selectedBeneficiaire);
            showAlert("Succès", "Bénéficiaire mis à jour avec succès.", Alert.AlertType.INFORMATION);
            loadData();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'âge doit être un nombre valide.", Alert.AlertType.ERROR);
        }
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
