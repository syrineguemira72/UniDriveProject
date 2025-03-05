package edu.unidrive.controllers;

import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.PostService;
import edu.unidrive.services.UserService;
import edu.unidrive.tools.JwtUtil;
import edu.unidrive.tools.MyConnection;
import io.jsonwebtoken.Claims;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeUniDriveController {

    @FXML
    private Button Profile;

    @FXML
    private Label logoutbtn;

    @FXML
    private ImageView profileImage;

    @FXML
    private Button statisticsButton;

    private String jwtToken;
    private Connection connection;
    @FXML
    private Label btnforum;
    @FXML
    private Button btnadmin;

    private final PostService postService = new PostService();

    @FXML
    void Logout(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutbtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String currentUserEmail;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        System.out.println("Email of the logged in user: " + currentUserEmail);
    }

    @FXML
    void goprofile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Profile.fxml"));
            Parent root = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setCurrentUserEmail(currentUserEmail);

            Stage stage = (Stage) Profile.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setProfileImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            profileImage.setImage(image);
        }
    }
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
        checkAdminAccess();
    }
    private boolean isAdmin(String token) {
        try {
            Claims claims = JwtUtil.validateToken(token);
            String role = claims.get("role", String.class);
            return "ADMIN".equals(role);
        } catch (Exception e) {
            return false; // Le token est invalide ou a expiré
        }
    }

    public void checkAdminAccess() {
        if (jwtToken != null) {
            boolean isAdmin = isAdmin(jwtToken);
            statisticsButton.setVisible(isAdmin);
        } else {
            statisticsButton.setVisible(false);
        }
    }


    @FXML
    void goToStatistics(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Statistics.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void initialize() {
        btnforum.setOnMouseClicked(this::forum);
    }
    private int getCurrentUserId() {
        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            return 53;
        }

        String query = "SELECT id FROM utilisateur WHERE email = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, currentUserEmail);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
        return 53;
    }


    @FXML
    void forum(MouseEvent event) {
        boolean isAdmin = isAdmin(jwtToken);

        if (!isAdmin) {
            int userId = getCurrentUserId();
            if (!postService.hasUserInterests(userId)) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CentresInteret.fxml"));
                    Parent root = fxmlLoader.load();
                    btnforum.getScene().setRoot(root);
                    return;
                } catch (IOException e) {
                    System.err.println("Erreur chargement centres d'intérêt : " + e.getMessage());
                }
            }
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomePost.fxml"));
            Parent root = fxmlLoader.load();
            HomePostControllers homePostControllers = fxmlLoader.getController();
            homePostControllers.setJwtToken(jwtToken);
            btnforum.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Erreur chargement HomePost : " + e.getMessage());
        }
    }
    @FXML
    void aide(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CRUD.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutbtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void covoiturage(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CreerTrajet.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutbtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void lost(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ObjetPerdu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutbtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}