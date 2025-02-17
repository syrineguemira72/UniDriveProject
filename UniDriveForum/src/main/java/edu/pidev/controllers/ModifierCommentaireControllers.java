package edu.pidev.controllers;

import edu.pidev.entities.Interaction;
import edu.pidev.entities.Post;
import edu.pidev.services.InteractionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ModifierCommentaireControllers {


    @FXML
    private TextField textcomment;

    private Interaction commentToUpdate;

    public void setCommentToUpdate(Interaction comment) {
        this.commentToUpdate = comment;
        textcomment.setText(comment.getContent()); // Pré-remplir le champ avec le contenu actuel du commentaire
    }
    private HomePostControllers homePostControllers;

    public void setHomePostControllers( HomePostControllers homePostControllers) {
        this.homePostControllers = homePostControllers;
    }



    @FXML
    void updatecomment(ActionEvent event) {
        if (commentToUpdate != null) {
            String newContent = textcomment.getText();
            commentToUpdate.setContent(newContent);

            InteractionService interactionService = new InteractionService();
            interactionService.updateEntity(commentToUpdate);

            // Afficher un message de succès
            showAlert("Succès", "Le commentaire a été mis à jour avec succès.");

            // Rediriger vers HomePostControllers
            if (homePostControllers != null) {
                homePostControllers.refreshPostList(); // Rafraîchir la liste des posts

                // Charger la scène HomePost.fxml
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePost.fxml"));
                    Parent root = fxmlLoader.load();

                    // Récupérer la scène actuelle
                    Scene scene = textcomment.getScene();
                    if (scene != null) {
                        scene.setRoot(root); // Définir la nouvelle racine
                    } else {
                        System.err.println("La scène actuelle est null.");
                    }
                } catch (IOException e) {
                    System.err.println("Erreur lors du chargement de HomePost.fxml : " + e.getMessage());
                }
            } else {
                System.err.println("homePostControllers est null.");
            }
        } else {
            showAlert("Erreur", "Aucun commentaire sélectionné pour la mise à jour.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }


}