package controller;

import entite.Objet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.ObjetService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class ObjetPerdu {
    @FXML
    private TableView<Objet> recTab;
    @FXML
    private TableColumn<Objet, Integer> idCol;
    @FXML
    private TableColumn<Objet, String> nomCol;
    @FXML
    private TableColumn<Objet, String> statusCol;
    @FXML
    private TableColumn<Objet, String> dateCol;
    @FXML
    private TableColumn<Objet, String> descriptionCol;
    @FXML
    private TableColumn<Objet, String> lieuCol;
    @FXML
    private TableColumn<Objet, String> categorieCol;

    @FXML
    private TextField filterField, idTf, nomTf, descriptionTf, categorieTf, imagePathTf;
    @FXML
    private DatePicker dateTf;

    private ObjetService objetService = new ObjetService();
    private ObservableList<Objet> objetList = FXCollections.observableArrayList();

    public ObjetPerdu() throws SQLException {
    }

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        lieuCol.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        categorieCol.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        loadData();
    }

    private void loadData() {
        try {
            objetList.clear();
            objetList.addAll(objetService.getAllObjets());
            recTab.setItems(objetList);
        } catch (Exception e) {
            showAlert("Database Error", "Failed to load data: " + e.getMessage());
        }
    }

    @FXML
    private void getSelected() {
        Objet selected = recTab.getSelectionModel().getSelectedItem();
        if (selected != null) {
            idTf.setText(String.valueOf(selected.getId()));
            nomTf.setText(selected.getNom());
            descriptionTf.setText(selected.getDescription());
            categorieTf.setText(selected.getCategorie());
            imagePathTf.setText(selected.getImagePath());
            try {
                dateTf.setValue(LocalDate.parse(selected.getDate()));
            } catch (Exception e) {
                dateTf.setValue(null);
                showAlert("Date Error", "Invalid date format for selected item.");
            }
        }
    }

    @FXML
    private void addObjet() {
        if (nomTf.getText().isEmpty() || dateTf.getValue() == null) {
            showAlert("Error", "Nom and Date are required fields.");
            return;
        }

        Objet objet = new Objet(
                nomTf.getText(),
                descriptionTf.getText(),
                dateTf.getValue().toString(),
                "Nouveau"
        );
        objet.setDescription(descriptionTf.getText());
        objet.setCategorie(categorieTf.getText());
        objet.setImagePath(imagePathTf.getText());
        try {
            objetService.addObjet(objet);
            loadData();
            clearFields();
            showAlert("Success", "Object added successfully!");
        } catch (Exception e) {
            showAlert("Database Error", "Failed to add object: " + e.getMessage());
        }
    }

    @FXML
    private void updateObjet() {
        if (idTf.getText().isEmpty() || nomTf.getText().isEmpty() || dateTf.getValue() == null) {
            showAlert("Error", "ID, Nom, and Date are required fields.");
            return;
        }

        Objet objet = new Objet();
        objet.setId(Integer.parseInt(idTf.getText()));
        objet.setNom(nomTf.getText());
        objet.setDate(dateTf.getValue().toString());
        objet.setDescription(descriptionTf.getText());
        objet.setLieu(descriptionTf.getText());
        objet.setCategorie(categorieTf.getText());
        objet.setImagePath(imagePathTf.getText());
        objet.setStatus("Mis à jour");
        try {
            objetService.updateObjet(objet);
            loadData();
            clearFields();
            showAlert("Success", "Object updated successfully!");
        } catch (Exception e) {
            showAlert("Database Error", "Failed to update object: " + e.getMessage());
        }
    }

    @FXML
    private void deleteObjet() {
        if (idTf.getText().isEmpty()) {
            showAlert("Error", "ID is required to delete an object.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTf.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID format. Please enter a valid number.");
            return;
        }

        try {
            objetService.deleteObjet(id);
            loadData();
            clearFields();
            showAlert("Success", "Object deleted successfully!");
        } catch (Exception e) {
            showAlert("Database Error", "Failed to delete object: " + e.getMessage());
        }
    }

    @FXML
    private void find() {
        if (idTf.getText().isEmpty()) {
            showAlert("Error", "ID is required to find an object.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTf.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID format. Please enter a valid number.");
            return;
        }

        try {
            Objet objet = objetService.findObjetById(id);
            if (objet != null) {
                objetList.clear();
                objetList.add(objet);
                recTab.setItems(objetList);
                showAlert("Success", "Object found with ID: " + id);
            } else {
                showAlert("Not Found", "No object found with ID: " + id);
            }
        } catch (Exception e) {
            showAlert("Database Error", "Failed to find object: " + e.getMessage());
        }
    }

    @FXML
    private void recherche() {
        String keyword = filterField.getText();
        try {
            objetList.clear();
            if (keyword.isEmpty()) {
                objetList.addAll(objetService.getAllObjets());
            } else {
                objetList.addAll(objetService.searchObjets(keyword));
            }
            recTab.setItems(objetList);
        } catch (Exception e) {
            showAlert("Database Error", "Failed to search objects: " + e.getMessage());
        }
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) recTab.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imagePathTf.setText(selectedFile.getAbsolutePath());
            showAlert("Success", "Image selected: " + selectedFile.getAbsolutePath());
        } else {
            showAlert("Info", "No image selected.");
        }
    }

    @FXML
    private void NavRecompense() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/recompence.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 736, 400);
            Stage stage = new Stage();
            stage.setTitle("Recompenses");
            stage.setScene(scene);
            stage.show();
            // Close the current window
            Stage currentStage = (Stage) recTab.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to load Recompense view: " + e.getMessage());
        }
    }

    @FXML
    private void CHAT() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chat.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Chat");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to load Chat view: " + e.getMessage());
        }
    }

    private void clearFields() {
        idTf.clear();
        nomTf.clear();
        descriptionTf.clear();
        categorieTf.clear();
        imagePathTf.clear();
        dateTf.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.contains("Error")) {
            alert.setAlertType(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}