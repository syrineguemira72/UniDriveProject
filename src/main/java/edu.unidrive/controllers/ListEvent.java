package edu.unidrive.controllers;

import edu.unidrive.entities.Event;
import edu.unidrive.services.EventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListEvent implements Initializable {

    private EventService rs = new EventService();
    @FXML
    private FlowPane cardlayout;

    @FXML
    private AnchorPane id_anchor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the list of events
        loadEventCards();

        // Set up the auto-refresh mechanism
        Timeline refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            loadEventCards();
        }));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    // Method to load event cards and display them
    private void loadEventCards() {
        List<Event> list = rs.getAllEvents();
        cardlayout.toFront();
        cardlayout.setHgap(20);
        cardlayout.setVgap(20);

        if (list.isEmpty()) {
            System.out.println("La liste des événements est vide.");
        } else {
            System.out.println("Nombre d'événements récupérés depuis la base de données : " + list.size());
            cardlayout.getChildren().clear(); // Clear existing cards before adding new ones
            for (Event event : list) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/cov/pidev3a8/hamza/EventCard.fxml"));
                    Pane cardView = loader.load();
                    EventCard controller = loader.getController();
                    controller.setEvent(event); // Set the event in the EventCard controller
                    cardlayout.getChildren().add(cardView);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @FXML
    void reload_page(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/cov/pidev3a8/hamza/EventForm.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
