package gestion_aide.controller;

import gestion_aide.entities.aide;
import gestion_aide.services.AideService;
import gestion_aide.entities.AideType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;


public class AjouterAide {

    @FXML
    private TextField typetextfield;

    @FXML
    private TextField descriptiontextfield;

    @FXML
    private TextField montanttextfield;

    private AideService aideService;  // Service class for interacting with the database

    public AjouterAide() {
        aideService = new AideService();
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

}

