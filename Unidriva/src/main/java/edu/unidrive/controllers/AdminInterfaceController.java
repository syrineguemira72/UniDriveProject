package edu.unidrive.controllers;

import edu.unidrive.entities.Interaction;
import edu.unidrive.entities.Post;
import edu.unidrive.services.InteractionService;
import edu.unidrive.services.PostService;
import edu.unidrive.services.TextFilterService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AdminInterfaceController {

    @FXML
    private ListView<String> badContentListView;

    private final PostService postService = new PostService();
    private final InteractionService interactionService = new InteractionService();
    private final TextFilterService textFilterService = new TextFilterService();

    @FXML
    public void initialize() {
        badContentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        loadBadContent();
    }

    private void loadBadContent() {
        List<Post> badPosts = postService.getPostsWithBadWords();
        for (Post post : badPosts) {
            badContentListView.getItems().add("Post: " + post.getTitle() + " - " + post.getDescription());
        }

        List<Interaction> badComments = interactionService.getCommentsWithBadWords();
        for (Interaction comment : badComments) {
            badContentListView.getItems().add("Commentaire: " + comment.getContent());
        }
    }

    @FXML
    void deleteSelectedItems() {
        List<String> selectedItems = badContentListView.getSelectionModel().getSelectedItems();

        if (selectedItems.isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner au moins un élément à supprimer.");
            return;
        }

        for (String item : selectedItems) {
            if (item.startsWith("Post:")) {
                String title = item.split(" - ")[0].replace("Post: ", "");
                Post post = postService.getPostByTitle(title);
                if (post != null) {
                    postService.removeEntity(post);
                }
            } else if (item.startsWith("Commentaire:")) {
                String content = item.replace("Commentaire: ", "");
                Interaction comment = interactionService.getCommentByContent(content);
                if (comment != null) {
                    interactionService.removeEntity(comment);
                }
            }
        }

        badContentListView.getItems().clear();
        loadBadContent();

        showAlert("Succès", "Les éléments sélectionnés ont été supprimés avec succès.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    @FXML
    void goToBack(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomeUniDrive.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur de navigation : " + e.getMessage());
        }
    }
}