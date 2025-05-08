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
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.File;
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
    @FXML
    void generatePdfButtonAction(ActionEvent event) {
        generatePdf(currencytextfield.getText(), descriptiontextfield.getText(), montanttextfield.getText(), "Statut inconnu");
    }


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

        if (currency.isBlank() || description.isBlank() || montant.isBlank()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        if (!AideType.isValidType(currency)) {
            showAlert("Erreur", "Currency must be 'Euro' , 'Dollar' or 'Dinar' .", Alert.AlertType.ERROR);
            return;
        }

        if (!montant.matches("\\d+(\\.\\d{1,2})?")) {
            showAlert("Erreur", "Le montant doit être un nombre valide (ex: 100 ou 100.50).", Alert.AlertType.ERROR);
            return;
        }

        String paymentMethodId = cardNumberField.getText().trim();
        if (paymentMethodId.isBlank()) {
            showAlert("Erreur", "Les informations de paiement doivent être complètes.", Alert.AlertType.ERROR);
            return;
        }

        //aide newAide = new aide(currency, description, montant);
        //aideService.addEntity(newAide);

        try {
            // Process Payment with Stripe
            String clientSecret = paymentService.createPaymentIntent(paymentMethodId, currency, montant);
            String paymentStatus = (clientSecret == null || clientSecret.isEmpty()) ? "Échec" : "Réussi";

            showAlert("Succès", "L'aide a été ajoutée avec succès et le paiement effectué.", Alert.AlertType.INFORMATION);

            // Génération du PDF après le paiement
            generatePdf(currency, description, montant, paymentStatus);

        } catch (Exception e) {
            showAlert("Erreur", "Paiement échoué: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void generatePdf(String currency, String description, String montant, String statut) {
        String pdfPath = "reçu_paiement.pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
// Start writing the title text
                //contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(200, 750);
                contentStream.showText("Reçu de Paiement");
                contentStream.endText(); // End the title text block

// Now, move to drawing mode to draw the separator line
                contentStream.setLineWidth(1f);
                contentStream.moveTo(100, 740); // Starting position for the separator line
                contentStream.lineTo(500, 740); // Ending position for the separator line
                contentStream.stroke(); // Actually draw the line

// Start a new text block to add receipt details
                contentStream.beginText();
               // contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 710);

// Add details
                contentStream.showText("Currency: " + currency);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Description: " + description);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Montant: " + montant + " USD");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Statut: " + statut);

// Add space at the bottom
                contentStream.newLineAtOffset(0, -30);

// End the text block first
                contentStream.endText(); // End the details text block

// Start drawing the footer line
                contentStream.setLineWidth(1f);
                contentStream.moveTo(100, 640); // Starting position for footer line
                contentStream.lineTo(500, 640); // Ending position for footer line
                contentStream.stroke(); // Draw the footer line

            }

            document.save(pdfPath);
            showAlert("Succès", "Le reçu PDF a été généré : " + pdfPath, Alert.AlertType.INFORMATION);

            // Ouvrir automatiquement le PDF après la génération
            java.awt.Desktop.getDesktop().open(new File(pdfPath));

        } catch (IOException ignored) {

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
    @FXML
    void goToBack(ActionEvent event) {
        // Load the new FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomeUniDrive.fxml"));
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
