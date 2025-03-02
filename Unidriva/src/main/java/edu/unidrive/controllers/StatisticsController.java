package edu.unidrive.controllers;

import edu.unidrive.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class StatisticsController {

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
}