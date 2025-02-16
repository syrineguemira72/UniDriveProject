package edu.pidev.controllers;

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
    public void start(Stage stage)  {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/HomePost.fxml"));//load fichier
            Scene scene = new Scene(root);//ntal3o scene
            stage.setScene(scene);//yatl3 aa scene
            stage.show();//yodhor
        } catch (IOException e) {
            System.err.println("eror"+e.getMessage());
        }
    }
}
