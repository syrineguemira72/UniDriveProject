package edu.unidrive.controllers;

import edu.unidrive.entities.Interaction;
import edu.unidrive.services.InteractionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.time.LocalDateTime;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import edu.unidrive.services.TextFilterService;


import java.time.LocalDate;

public class Ajoutercommentairecontrollers {

    @FXML
    private TextField commentid;
    private int postId;
    private int currentUserId;

    private final TextFilterService textFilterService = new TextFilterService();


    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void commentaction(ActionEvent event) {
        String content = commentid.getText().trim();

        if (content.isEmpty()) {
            showAlert("Erreur", "Veuillez saisir un commentaire.");
            return;
        }

        String filteredContent = textFilterService.filterBadWords(content);

        LocalDateTime created_at = LocalDateTime.now();  // Changed to LocalDateTime
        Interaction interaction = new Interaction(filteredContent, created_at, postId, currentUserId);

        InteractionService interactionService = new InteractionService();
        try {
            interactionService.addEntity(interaction);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Le commentaire a été ajouté avec succès !");
            alert.showAndWait();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePost.fxml"));
            Parent root = fxmlLoader.load();
            commentid.getScene().setRoot(root);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout du commentaire : " + e.getMessage());
        }
    }
}