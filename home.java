package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class home extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(home.class.getResource("/ObjetPerdu.fxml"));
        if (fxmlLoader.getLocation() == null) {
            throw new IOException("Cannot find ObjetPerdu.fxml. Ensure it is in src/main/resources/");
        }
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        primaryStage.setTitle("Objet Perdu Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}