package edu.pidev.controllers;

import edu.pidev.entities.Interaction;
import edu.pidev.services.InteractionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;

public class Ajoutercommentairecontrollers {

    @FXML
    private TextField commentid; // Champ pour saisir le commentaire

    private int postId;

    public void setPostId(int postId) {
        this.postId = postId;
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

        LocalDate date = LocalDate.now(); // Date actuelle
        Interaction interaction = new Interaction(content, date, postId);

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