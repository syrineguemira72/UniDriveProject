package edu.unidrive.controllers;
import edu.unidrive.entities.Interaction;
import edu.unidrive.services.InteractionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import edu.unidrive.services.TextFilterService;


import java.io.IOException;

public class ModifierCommentaireControllers {


    @FXML
    private TextField textcomment;

    private Interaction commentToUpdate;
    private final TextFilterService textFilterService = new TextFilterService();


    public void setCommentToUpdate(Interaction comment) {
        this.commentToUpdate = comment;
        textcomment.setText(comment.getContent());
    }
    private edu.unidrive.controllers.HomePostControllers homePostControllers;

    public void setHomePostControllers( edu.unidrive.controllers.HomePostControllers homePostControllers) {
        this.homePostControllers = homePostControllers;
    }



    @FXML
    void updatecomment(ActionEvent event) {
        if (commentToUpdate != null) {
            String newContent = textcomment.getText();
            String filteredContent = textFilterService.filterBadWords(newContent);

            commentToUpdate.setContent(filteredContent);
            InteractionService interactionService = new InteractionService();
            interactionService.updateEntity(commentToUpdate);
            showAlert("Succès", "Le commentaire a été mis à jour avec succès.");
            if (homePostControllers != null) {
                homePostControllers.refreshPostList();

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePost.fxml"));
                    Parent root = fxmlLoader.load();


                    Scene scene = textcomment.getScene();
                    if (scene != null) {
                        scene.setRoot(root);
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