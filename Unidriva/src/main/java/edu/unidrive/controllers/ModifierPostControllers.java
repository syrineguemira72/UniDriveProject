package edu.unidrive.controllers;

import edu.unidrive.entities.Post;
import edu.unidrive.services.PostService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import edu.unidrive.services.TextFilterService;


import java.io.IOException;

public class ModifierPostControllers {

    @FXML
    private TextField descriptionmo;

    @FXML
    private TextField titlemo;

    @FXML
    private Button updatepost;

    private Post postToUpdate;
    private edu.unidrive.controllers.HomePostControllers homePostControllers;
    private final TextFilterService textFilterService = new TextFilterService();


    public void setPostToUpdate(Post post) {
        this.postToUpdate = post;
        titlemo.setText(post.getTitle());
        descriptionmo.setText(post.getDescription());
    }

    public void setHomePostControllers(edu.unidrive.controllers.HomePostControllers homePostControllers) {
        this.homePostControllers = homePostControllers;
    }

    @FXML
    void updatepost (ActionEvent event) {
        String newTitle = titlemo.getText();
        String newDescription = descriptionmo.getText();
        String filteredTitle = textFilterService.filterBadWords(newTitle);
        String filteredDescription = textFilterService.filterBadWords(newDescription);

        postToUpdate.setTitle(filteredTitle);
        postToUpdate.setDescription(filteredDescription);

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