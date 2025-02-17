package edu.pidev.controllers;

import edu.pidev.entities.Interaction;
import edu.pidev.entities.Post;
import edu.pidev.services.InteractionService;
import edu.pidev.services.PostService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HomePostControllers {
    @FXML
    private ListView<Post> postListView; // ListView pour afficher les posts

    private ObservableList<Post> postList = FXCollections.observableArrayList(); // Liste observable pour les posts

    private final PostService postService = new PostService();

    @FXML
    public void initialize() {
        // Charger les posts au démarrage
        refreshPostList();
        // Configurer la ListView pour afficher les posts et les commentaires
        postListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Post> call(ListView<Post> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Post post, boolean empty) {
                        super.updateItem(post, empty);

                        if (empty || post == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            // Créer un conteneur pour le post et les commentaires
                            VBox container = new VBox(10);

                            // Afficher le post
                            Text postText = new Text(post.getTitle() + " : " + post.getDescription());
                            container.getChildren().add(postText);

                            // Bouton "Commenter"
                            Button commentButton = new Button("Commenter");
                            commentButton.setOnAction(event -> {
                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Ajoutercommentaire.fxml"));
                                    Parent root = fxmlLoader.load();

                                    // Passer l'ID du post à Ajoutercommentairecontrollers
                                    Ajoutercommentairecontrollers ajouterCommentaireControllers = fxmlLoader.getController();
                                    ajouterCommentaireControllers.setPostId(post.getId());

                                    // Changer la scène actuelle
                                    postListView.getScene().setRoot(root);
                                } catch (IOException e) {
                                    System.err.println("Erreur lors du chargement de Ajoutercommentaire.fxml : " + e.getMessage());
                                }
                            });
                            container.getChildren().add(commentButton);

                            // Afficher les commentaires
                            List<Interaction> comments = postService.getCommentsByPostId(post.getId());
                            for (Interaction comment : comments) {
                                Text commentText = new Text("Commentaire : " + comment.getContent());
                                container.getChildren().add(commentText);

                                // Créer un HBox pour les boutons "Supprimer" et "Modifier"
                                HBox buttonContainer = new HBox(10); // 10 est l'espacement entre les boutons

                                // Bouton "Supprimer"
                                Button deleteCommentButton = new Button("Supprimer");
                                deleteCommentButton.setOnAction(event -> {
                                    InteractionService interactionService = new InteractionService();
                                    interactionService.removeEntity(comment);
                                    refreshPostList(); // Rafraîchir la liste après suppression
                                });

                                // Bouton "Modifier"
                                Button updateCommentButton = new Button("Modifier");
                                updateCommentButton.setOnAction(event -> {
                                    try {
                                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ModifierCommentaire.fxml"));
                                        Parent root = fxmlLoader.load();

                                        // Passer le commentaire à modifier à ModifierCommentaireControllers

                                        ModifierCommentaireControllers modifierCommentaireControllers = fxmlLoader.getController();


                                        modifierCommentaireControllers.setCommentToUpdate(comment);
                                        modifierCommentaireControllers.setHomePostControllers(HomePostControllers.this);

                                        // Changer la scène actuelle
                                        postListView.getScene().setRoot(root);
                                    } catch (IOException e) {
                                        System.err.println("Erreur lors du chargement de ModifierCommentaire.fxml : " + e.getMessage());
                                    }
                                });

                                // Ajouter les boutons au HBox
                                buttonContainer.getChildren().addAll(deleteCommentButton, updateCommentButton);

                                // Ajouter le HBox au VBox principal
                                container.getChildren().add(buttonContainer);
                            }

                            setGraphic(container);
                        }
                    }
                };
            }
        });
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
        Post selectedPost = postListView.getSelectionModel().getSelectedItem();

        if (selectedPost == null) {
            showAlert("Erreur", "Veuillez sélectionner un post à modifier.");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ModifierPost.fxml"));
            Parent root = fxmlLoader.load();

            // Récupérer le contrôleur de la page ModifierPost
            ModifierPostControllers modifierPostControllers = fxmlLoader.getController();

            // Passer les données du post sélectionné à ModifierPostControllers
            modifierPostControllers.setPostToUpdate(selectedPost);
            modifierPostControllers.setHomePostControllers(this);

            postListView.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ModifierPost.fxml : " + e.getMessage());
        }
    }

    @FXML
    void removee(ActionEvent event) {
        Post selectedPost = postListView.getSelectionModel().getSelectedItem();

        if (selectedPost == null) {
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
            postService.removeEntity(selectedPost);

            // Rafraîchir la liste après suppression
            refreshPostList();
            showAlert("Succès", "Le post a été supprimé avec succès.");
        }
    }

    public void refreshPostList() {
        postList.clear(); // Vider la liste actuelle
        List<Post> posts = postService.getAllData(); // Récupérer les posts depuis le service
        postList.addAll(posts); // Ajouter les nouveaux posts à la liste observable
        postListView.setItems(postList); // Mettre à jour la ListView
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}