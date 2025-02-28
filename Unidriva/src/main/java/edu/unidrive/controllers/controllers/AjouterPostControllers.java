package edu.unidrive.controllers.controllers;

import edu.unidrive.entities.Post;
import edu.unidrive.services.PostService;
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

    private HomePostControllers homePostControllers;

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

        Post post = new Post(description, title);
        PostService postservice = new PostService();
        postservice.addEntity(post);

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