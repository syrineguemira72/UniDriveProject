package edu.unidrive.services;

import edu.unidrive.entities.Activity;
import edu.unidrive.interfaces.IActivityService;
import edu.unidrive.tools.MyConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityService implements IActivityService {

    private Connection cnx;

    public ActivityService() {
        cnx = MyConnection.getInstance().getCnx();
        if (cnx == null) {
            System.out.println("Error: Database connection is NULL in ActivityService!");
        } else {
            System.out.println("Database connection established successfully in ActivityService.");
        }
    }

    @Override


    public void addActivity(Activity activity) {
        // Validate the fields
        if (!isTitreValid(activity.getTitre())) {
            showAlert("Invalid Title", "The title must contain only alphabetic characters.");
            return; // Stop execution if validation fails
        }

        if (activity.getDescription().isEmpty()) {
            showAlert("Invalid Description", "The description cannot be empty.");
            return; // Stop execution if validation fails
        }

        if (activity.getDate() == null) {
            showAlert("Invalid Date", "The date cannot be empty.");
            return; // Stop execution if validation fails
        }

        // If validation passes, proceed with adding the activity
        String query = "INSERT INTO activity (titre, description, date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, activity.getTitre());
            ps.setString(2, activity.getDescription());
            ps.setDate(3, new java.sql.Date(activity.getDate().getTime()));
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                activity.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Error adding activity: " + e.getMessage());
        }
    }

    // Method to validate the titre field (only alphabetic characters)
    private boolean isTitreValid(String titre) {
        return titre != null && titre.matches("[a-zA-Z]+");
    }

    // Method to show an alert message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }


    @Override
    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();
        String query = "SELECT * FROM activity";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Activity activity = new Activity();
                activity.setId(rs.getInt("id"));
                activity.setTitre(rs.getString("titre"));
                activity.setDescription(rs.getString("description"));
                activity.setDate(rs.getDate("date"));
                activities.add(activity);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching activities: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public Activity getActivityById(int id) {
        String query = "SELECT * FROM activity WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Activity activity = new Activity();
                activity.setId(rs.getInt("id"));
                activity.setTitre(rs.getString("titre"));
                activity.setDescription(rs.getString("description"));
                activity.setDate(rs.getDate("date"));
                return activity;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching activity by ID: " + e.getMessage());
        }
        return null;
    }

    public Activity getActivityByTitle(String titre) {
        String query = "SELECT * FROM activity WHERE titre = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, titre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Activity(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getDate("date")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching activity by title: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateActivity(Activity activity) {
        String query = "UPDATE activity SET titre = ?, description = ?, date = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, activity.getTitre());
            ps.setString(2, activity.getDescription());
            ps.setDate(3, new java.sql.Date(activity.getDate().getTime()));
            ps.setInt(4, activity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating activity: " + e.getMessage());
        }
    }

    @Override
    public void deleteActivity(int id) {
        String query = "DELETE FROM activity WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting activity: " + e.getMessage());
        }
    }
}