package edu.unidrive.controllers;

import edu.unidrive.entities.Interaction;
import edu.unidrive.services.InteractionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import edu.unidrive.services.TextFilterService;


import java.time.LocalDate;

public class Ajoutercommentairecontrollers {

    @FXML
    private TextField commentid;

    private int postId;
    private final TextFilterService textFilterService = new TextFilterService();


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

        // Vérifier que le commentaire n'est pas vide
        if (content.isEmpty()) {
            showAlert("Erreur", "Veuillez saisir un commentaire.");
            return;
        }

        // Filtrer les mots inappropriés dans le commentaire
        String filteredContent = textFilterService.filterBadWords(content);

        // Créer un nouvel objet Interaction avec le contenu filtré
        LocalDate date = LocalDate.now();
        Interaction interaction = new Interaction(filteredContent, date, postId);

        // Ajouter le commentaire à la base de données
        InteractionService interactionService = new InteractionService();
        try {
            interactionService.addEntity(interaction);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Le commentaire a été ajouté avec succès !");
            alert.showAndWait();

            // Recharger la page HomePost.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePost.fxml"));
            Parent root = fxmlLoader.load();
            commentid.getScene().setRoot(root);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout du commentaire : " + e.getMessage());
        }
    }
}