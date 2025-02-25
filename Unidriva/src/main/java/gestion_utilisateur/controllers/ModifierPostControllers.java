package gestion_forum.controllers;

import gestion_forum.entities.Post;
import gestion_forum.services.PostService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ModifierPostControllers {

    @FXML
    private TextField descriptionmo;

    @FXML
    private TextField titlemo;

    @FXML
    private Button updatepost;

    private Post postToUpdate;
    private HomePostControllers homePostControllers;

    public void setPostToUpdate(Post post) {
        this.postToUpdate = post;
        titlemo.setText(post.getTitle());
        descriptionmo.setText(post.getDescription());
    }

    public void setHomePostControllers(HomePostControllers homePostControllers) {
        this.homePostControllers = homePostControllers;
    }

    @FXML
    void updatepost (ActionEvent event) {
        String newTitle = titlemo.getText();
        String newDescription = descriptionmo.getText();

        postToUpdate.setTitle(newTitle);
        postToUpdate.setDescription(newDescription);

        PostService postservice = new PostService();
        postservice.updateEntity(postToUpdate);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText("Le post a été modifié avec succès : " + postToUpdate);
        alert.showAndWait();

        if (homePostControllers != null) {
            homePostControllers.refreshPostList();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePost.fxml"));
            Parent root = fxmlLoader.load();
            titlemo.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de HomePost.fxml : " + e.getMessage());
        }
    }
}