package edu.unidrive.controllers;

import edu.unidrive.entities.aide;
import edu.unidrive.entities.AideType;
import edu.unidrive.services.AideService;
import edu.unidrive.services.PaymentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class AjouterAide {

    @FXML private TextField currencyField;
    @FXML private TextField descriptionField;
    @FXML private TextField montantField;
    @FXML private TextField paymentTokenField;  // e.g., card token or payment method id

    private final AideService aideService;
    private final PaymentService paymentService;

    public AjouterAide() {
        this.aideService = new AideService();
        this.paymentService = new PaymentService();
    }

    @FXML
    void ajouterAideaction(ActionEvent event) {
        String currency = currencyField.getText().trim();
        String description = descriptionField.getText().trim();
        String montantStr = montantField.getText().trim();
        String paymentToken = paymentTokenField.getText().trim();

        if (currency.isBlank() || description.isBlank() || montantStr.isBlank() || paymentToken.isBlank()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        if (!AideType.isValidType(currency)) {
            showAlert("Erreur", "Currency must be 'Euro', 'Dollar' or 'Dinar'.", Alert.AlertType.ERROR);
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(montantStr);
            if (montant <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.", Alert.AlertType.ERROR);
            return;
        }

        // Save aide entity first
        aide newAide = new aide(currency, description, String.valueOf(montant),
                LocalDate.now().toString(), /* idUser= */0, /* assocId= */0);
        aideService.addEntity(newAide);

        // Process payment
        boolean paid = paymentService.payerAvecStripe(montant, paymentToken);
        String status = paid ? "Réussi" : "Échec";

        if (paid) {
            showAlert("Succès", "Aide ajoutée et paiement réussi.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Le paiement a échoué.", Alert.AlertType.ERROR);
        }

        // Optionally generate PDF receipt here
        // generatePdfReceipt(newAide, status);
    }

    // Navigation
    @FXML
    void goToAdminPage(ActionEvent event) {
        navigate(event, "/AideAdminPage.fxml");
    }

    @FXML
    void goToBack(ActionEvent event) {
        navigate(event, "/HomeUniDrive.fxml");
    }

    private void navigate(ActionEvent event, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void goToAnotherPage(ActionEvent event) {
        // exactly the same signature you’ve used elsewhere:
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AideAdminPage.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}