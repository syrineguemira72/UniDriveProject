package gestion_forum.controllers;
import gestion_forum.entities.Interaction;
import gestion_forum.entities.Post;
import gestion_forum.services.InteractionService;
import gestion_forum.services.PostService;
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
    private ListView<Post> postListView;

    private ObservableList<Post> postList = FXCollections.observableArrayList();

    private final PostService postService = new PostService();

    @FXML
    public void initialize() {
        refreshPostList();
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
                            VBox container = new VBox(10);

                            Text postText = new Text(post.getTitle() + " : " + post.getDescription());
                            container.getChildren().add(postText);

                            Button commentButton = new Button("Commenter");
                            commentButton.setOnAction(event -> {
                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Ajoutercommentaire.fxml"));
                                    Parent root = fxmlLoader.load();

                                    Ajoutercommentairecontrollers ajouterCommentaireControllers = fxmlLoader.getController();
                                    ajouterCommentaireControllers.setPostId(post.getId());

                                    postListView.getScene().setRoot(root);
                                } catch (IOException e) {
                                    System.err.println("Erreur lors du chargement de Ajoutercommentaire.fxml : " + e.getMessage());
                                }
                            });
                            container.getChildren().add(commentButton);

                            List<Interaction> comments = postService.getCommentsByPostId(post.getId());
                            for (Interaction comment : comments) {
                                Text commentText = new Text("Commentaire : " + comment.getContent());
                                container.getChildren().add(commentText);

                                HBox buttonContainer = new HBox(10);

                                Button deleteCommentButton = new Button("Supprimer");
                                deleteCommentButton.setOnAction(event -> {
                                    InteractionService interactionService = new InteractionService();
                                    interactionService.removeEntity(comment);
                                    refreshPostList();
                                });

                                Button updateCommentButton = new Button("Modifier");
                                updateCommentButton.setOnAction(event -> {
                                    try {
                                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ModifierCommentaire.fxml"));
                                        Parent root = fxmlLoader.load();
                                        ModifierCommentaireControllers modifierCommentaireControllers = fxmlLoader.getController();
                                        modifierCommentaireControllers.setCommentToUpdate(comment);
                                        modifierCommentaireControllers.setHomePostControllers(HomePostControllers.this);
                                        postListView.getScene().setRoot(root);
                                    } catch (IOException e) {
                                        System.err.println("Erreur lors du chargement de ModifierCommentaire.fxml : " + e.getMessage());
                                    }
                                });
                                buttonContainer.getChildren().addAll(deleteCommentButton, updateCommentButton);
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
            AjouterPostControllers ajouterPostControllers = fxmlLoader.getController();
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
            ModifierPostControllers modifierPostControllers = fxmlLoader.getController();
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
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer ce post ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            postService.removeEntity(selectedPost);
            refreshPostList();
            showAlert("Succès", "Le post a été supprimé avec succès.");
        }
    }

    public void refreshPostList() {
        postList.clear();
        List<Post> posts = postService.getAllData();
        postList.addAll(posts);
        postListView.setItems(postList);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}