package edu.unidrive.controllers;

import edu.unidrive.entities.AideType;
import edu.unidrive.entities.aide;
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
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;


public class AjouterAide {

    @FXML
    private WebView paymentWebView; // WebView to load Stripe Elements
    @FXML
    private TextField expiryField;
    @FXML
    private TextField cvcField;
    @FXML
    private TextField cardNumberField;
    @FXML
    private TextField currencytextfield;

    @FXML
    private TextField descriptiontextfield;

    @FXML
    private TextField montanttextfield;

    private AideService aideService;  // Service class for interacting with the database

    private PaymentService paymentService;  // Declare PaymentService

    public AjouterAide()
    {
        aideService = new AideService();
        paymentService = new PaymentService();  // Initialize PaymentService
    }

    @FXML
    void ajouterAideaction(ActionEvent event) {
        String currency = currencytextfield.getText().trim();
        String description = descriptiontextfield.getText().trim();
        String montant = montanttextfield.getText().trim();

        // Validate Empty Fields
        if (currency.isBlank() || description.isBlank() || montant.isBlank()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        // Validate Currency Type
        if (!AideType.isValidType(currency)) {
            showAlert("Erreur", "La devise doit être 'alimentaire', 'financier', ou 'médical'.", Alert.AlertType.ERROR);
            return;
        }

        // Validate Montant (amount should be a valid number)
        if (!montant.matches("\\d+(\\.\\d{1,2})?")) {
            showAlert("Erreur", "Le montant doit être un nombre valide (ex: 100 ou 100.50).", Alert.AlertType.ERROR);
            return;
        }

        // Get PaymentMethod ID (This should be passed from the frontend)
        String paymentMethodId = cardNumberField.getText().trim();  // PaymentMethod ID, not raw card details

        // Validate PaymentMethod ID
        if (paymentMethodId.isBlank()) {
            showAlert("Erreur", "Les informations de paiement doivent être complètes.", Alert.AlertType.ERROR);
            return;
        }

        // Add aide to the database first (before payment)
        aide aide = new aide(currency, description, montant);
        aideService.addEntity(aide);

        try {
            // Process Payment with Stripe (using PaymentMethod ID)
            String clientSecret = paymentService.createPaymentIntent(paymentMethodId, currency, montant);

            // Check if the payment creation was successful
            if (clientSecret == null || clientSecret.isEmpty()) {
                showAlert("Succès", "\"La aide et le paiement ont été ajoutés, mais il y a un petit problème de validation du numéro de carte avec Stripe.\"", Alert.AlertType.INFORMATION);
            } else {
                // If payment is successful, show success message
                showAlert("Succès", "L'aide a été ajoutée avec succès et le paiement effectué.", Alert.AlertType.INFORMATION);
            }

        } catch (Exception e) {
            // Show payment failure alert but still let the aide be added to the database
            showAlert("Erreur", "Paiement échoué: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        // Navigate to Detail Page
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Detail.fxml"));
            Parent root = fxmlLoader.load();
            DetailController detailController = fxmlLoader.getController();
            detailController.setCurrencytextfield(currency);
            detailController.setDescriptiontextfield(description);
            detailController.setMontanttextfield(montant);
            currencytextfield.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la page de détails.", Alert.AlertType.ERROR);
        }
    }







    @FXML
    void goToAnotherPage(ActionEvent event) {
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

    // Helper method to show alerts
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
