package edu.unidrive.controllers;
import edu.unidrive.services.PostService;
import edu.unidrive.services.TrajetService;
import edu.unidrive.services.UserService;
import edu.unidrive.entities.Trajet; // Ajoutez cette ligne
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class StatisticsController {
    @FXML
    private Button backBtn;

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label adminUsersLabel;

    @FXML
    private Label normalUsersLabel;

    @FXML
    private Label totalTripsLabel;

    @FXML
    private Label availableSeatsLabel;

    @FXML
    private Label totalPostsLabel;

    @FXML
    private Label postsWithBadWordsLabel;

    @FXML
    private PieChart usersPieChart;
    @FXML
    private BarChart<String, Number> postsBarChart;

    @FXML
    private BarChart<String, Number> tripsBarChart;

    private UserService userService = new UserService();
    private PostService postService = new PostService();
    private TrajetService trajetService = new TrajetService(); // Ajoutez cette ligne

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        // Récupérer les statistiques des utilisateurs
        int totalUsers = userService.getTotalUsers();
        int adminUsers = userService.getAdminUsers();
        int normalUsers = totalUsers - adminUsers;

        totalUsersLabel.setText("Nombre total d'utilisateurs : " + totalUsers);
        adminUsersLabel.setText("Nombre d'administrateurs : " + adminUsers);
        normalUsersLabel.setText("Nombre d'utilisateurs normaux : " + normalUsers);

        // Ajouter les données au PieChart
        PieChart.Data adminData = new PieChart.Data("Administrateurs", adminUsers);
        PieChart.Data normalData = new PieChart.Data("Utilisateurs normaux", normalUsers);
        usersPieChart.getData().addAll(adminData, normalData);

        int totalPosts = postService.getAllData().size();
        int postsWithBadWords = postService.getPostsWithBadWords().size();

        totalPostsLabel.setText("Nombre total de posts : " + totalPosts);
        postsWithBadWordsLabel.setText("Nombre de posts avec des mots inappropriés : " + postsWithBadWords);

        XYChart.Series<String, Number> postsSeries = new XYChart.Series<>();
        postsSeries.getData().add(new XYChart.Data<>("Total Posts", totalPosts));
        postsSeries.getData().add(new XYChart.Data<>("Posts avec mots inappropriés", postsWithBadWords));
        postsBarChart.getData().add(postsSeries);

        int totalTrips = trajetService.getAllData().size();
        int totalAvailableSeats = trajetService.getAllData().stream()
                .mapToInt(Trajet::getPlaceDisponible)
                .sum();

        totalTripsLabel.setText("Nombre total de trajets : " + totalTrips);
        availableSeatsLabel.setText("Nombre total de places disponibles : " + totalAvailableSeats);
        XYChart.Series<String, Number> tripsSeries = new XYChart.Series<>();
        tripsSeries.getData().add(new XYChart.Data<>("Total Trajets", totalTrips));
        tripsSeries.getData().add(new XYChart.Data<>("Places disponibles", totalAvailableSeats));
        tripsBarChart.getData().add(tripsSeries);
    }
    @FXML
    void back(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/HomeUnidrive.fxml"));
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