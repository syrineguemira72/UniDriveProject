package edu.unidrive;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);  // Lancer l'application JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML pour l'interface utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObjetPerdu.fxml"));
            Parent root = loader.load();

            // Définir la scène et l'initialiser
            Scene scene = new Scene(root);
            primaryStage.setTitle("UniDrive Application");  // Titre de la fenêtre
            primaryStage.setScene(scene);  // Appliquer la scène à la fenêtre
            primaryStage.show();  // Afficher la fenêtre
        } catch (Exception e) {
            e.printStackTrace();  // Afficher les erreurs éventuelles
        }
    }
}