package edu.unidrive.controllers;

import edu.unidrive.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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
    private Label totalPostsLabel;

    @FXML
    private PieChart usersPieChart;

    private UserService userService = new UserService();


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
    }
    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("HomeUnidrive.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}