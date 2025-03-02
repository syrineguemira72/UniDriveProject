package edu.unidrive.controllers;

import edu.unidrive.entities.Post;
import edu.unidrive.services.PostService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import edu.unidrive.services.TextFilterService;


import java.io.IOException;

public class AjouterPostControllers {

    @FXML
    private TextField descriptionpost;

    @FXML
    private TextField titlepost;

    private edu.unidrive.controllers.HomePostControllers homePostControllers;
    private final TextFilterService textFilterService = new TextFilterService();
    private final PostService postService = new PostService(); // Déclarer postService

    public void setHomePostControllers(edu.unidrive.controllers.HomePostControllers homePostControllers) {
        this.homePostControllers = homePostControllers;
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void ajouterpost (ActionEvent event) {
        String description = descriptionpost.getText();
        String title = titlepost.getText();
        if (title.isEmpty() || description.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }


        // Filtrer les mots inappropriés dans le titre et la description
        String filteredTitle = textFilterService.filterBadWords(title);
        String filteredDescription = textFilterService.filterBadWords(description);

        // Créer un nouveau post avec le texte filtré
        Post post = new Post(filteredTitle, filteredDescription);
        PostService postservice = new PostService();
        postService.addEntity(post);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText("Le post a été ajouté avec succès : " + post);
        alert.showAndWait();

        if (homePostControllers != null) {
            homePostControllers.refreshPostList();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePost.fxml"));
            Parent root = fxmlLoader.load();
            titlepost.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de HomePost.fxml : " + e.getMessage());
        }
    }
}