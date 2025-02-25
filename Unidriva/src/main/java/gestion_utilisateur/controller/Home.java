package gestion_utilisateur.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Home extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/CRUD.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ex) {
            System.err.println("Erreur de lecture"+ ex.getMessage());
        }
    }
}
