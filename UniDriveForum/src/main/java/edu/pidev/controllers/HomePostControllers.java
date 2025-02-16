package edu.pidev.controllers;

import edu.pidev.entities.Post;
import edu.pidev.services.PostService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HomePostControllers {
    @FXML
    private Label homepage;
    @FXML
    private ListView<String> postListView; // ListView pour afficher les posts

    private ObservableList<String> postList = FXCollections.observableArrayList(); // Liste observable pour les posts

    private final PostService postService = new PostService();

    @FXML
    public void initialize() {
        // Charger les posts au démarrage
        refreshPostList();
    }

    @FXML
    void createe(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AjouterPost.fxml"));
            Parent root = fxmlLoader.load();

            // Récupérer le contrôleur de la page AjouterPost
            AjouterPostControllers ajouterPostControllers = fxmlLoader.getController();

            // Passer la référence de ce contrôleur à AjouterPostControllers
            ajouterPostControllers.setHomePostControllers(this);

            postListView.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de AjouterPost.fxml : " + e.getMessage());
        }
    }

    @FXML
    void updatee(ActionEvent event) {
        String selectedPostText = postListView.getSelectionModel().getSelectedItem();

        if (selectedPostText == null) {
            showAlert("Erreur", "Veuillez sélectionner un post à modifier.");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ModifierPost.fxml"));
            Parent root = fxmlLoader.load();

            // Récupérer le contrôleur de la page ModifierPost
            ModifierPostControllers modifierPostControllers = fxmlLoader.getController();

            // Passer les données du post sélectionné à ModifierPostControllers
            int postId = extractIdFromText(selectedPostText);
            Post postToUpdate = postService.getById(postId);
            modifierPostControllers.setPostToUpdate(postToUpdate);
            modifierPostControllers.setHomePostControllers(this);

            postListView.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ModifierPost.fxml : " + e.getMessage());
        }
    }

    @FXML
    void removee(ActionEvent event) {
        String selectedPostText = postListView.getSelectionModel().getSelectedItem();

        if (selectedPostText == null) {
            showAlert("Erreur", "Veuillez sélectionner un post à supprimer.");
            return;
        }

        // Afficher une confirmation avant de supprimer
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer ce post ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int postId = extractIdFromText(selectedPostText);
            Post postToDelete = new Post();
            postToDelete.setId(postId);
            postService.removeEntity(postToDelete);

            // Rafraîchir la liste après suppression
            refreshPostList();
            showAlert("Succès", "Le post a été supprimé avec succès.");
        }
    }

    // Méthode pour extraire l'ID du texte affiché (ex: "5 - Mon Post")
    private int extractIdFromText(String text) {
        String[] parts = text.split(" - ");
        return Integer.parseInt(parts[0]);
    }

    // Méthode pour rafraîchir la liste des posts
    public void refreshPostList() {
        postList.clear();
        List<Post> posts = postService.getAllData();

        for (Post post : posts) {
            postList.add(post.getId() + " - " + post.getTitle() + " : " + post.getDescription());
        }

        postListView.setItems(postList);
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}