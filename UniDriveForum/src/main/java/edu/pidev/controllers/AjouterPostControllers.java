package edu.pidev.controllers;

import edu.pidev.entities.Post;
import edu.pidev.services.PostService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AjouterPostControllers {

    @FXML
    private TextField descriptionpost;

    @FXML
    private TextField titlepost;

    private HomePostControllers homePostControllers; // Référence à HomePostControllers

    // Méthode pour définir la référence à HomePostControllers
    public void setHomePostControllers(HomePostControllers homePostControllers) {
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

        // Créer un nouveau post
        Post post = new Post(description, title);
        PostService postservice = new PostService();
        postservice.addEntity(post);

        // Afficher une alerte de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText("Le post a été ajouté avec succès : " + post);
        alert.showAndWait();

        // Rafraîchir la ListView dans HomePostControllers
        if (homePostControllers != null) {
            homePostControllers.refreshPostList();
        }

        // Revenir à la page d'accueil
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePost.fxml"));
            Parent root = fxmlLoader.load();
            titlepost.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de HomePost.fxml : " + e.getMessage());
        }
    }
}