package edu.unidrive.controllers;

import edu.unidrive.entities.Post;
import edu.unidrive.services.PostService;
import edu.unidrive.tools.MyConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import edu.unidrive.services.TextFilterService;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AjouterPostControllers {

    @FXML
    private TextField descriptionpost;

    @FXML
    private TextField titlepost;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    public void initialize() {
        // Initialiser les catégories disponibles
        categoryComboBox.getItems().addAll(
                "Web Developement",
                "Mobile Developement",
                "Data Science",
                "Cyber Security",
                "IA",
                "Others"
        );
    }
    private edu.unidrive.controllers.HomePostControllers homePostControllers;
    private final TextFilterService textFilterService = new TextFilterService();
    private final PostService postService = new PostService();


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

    private String currentUserEmail; // Ajoutez cette ligne

    // Ajoutez cette méthode pour définir l'email
    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }

    @FXML
    void ajouterpost(ActionEvent event) {
        String description = descriptionpost.getText();
        String title = titlepost.getText();
        String category = categoryComboBox.getValue();

        if (title.isEmpty() || description.isEmpty() || category == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        Post post = new Post(title, description);
        post.setCategory(category);
        post.setUserId(getCurrentUserId()); // Définir l'ID utilisateur

        postService.addEntity(post);

        // Redirection vers la page d'accueil
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePost.fxml"));
            Parent root = loader.load();
            titlepost.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getCurrentUserId() {
        // Implémentez cette méthode pour récupérer l'ID de l'utilisateur connecté
        // Exemple basique (à adapter à votre système d'authentification):
        String query = "SELECT id FROM utilisateur WHERE email = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, currentUserEmail); // currentUserEmail doit être défini
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // ou gérer l'erreur différemment
    }
}