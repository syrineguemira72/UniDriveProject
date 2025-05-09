package edu.unidrive.controllers;

import edu.unidrive.entities.Objet;
import edu.unidrive.services.CloudinaryService;
import edu.unidrive.services.ObjetPerduService;
import edu.unidrive.tools.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjetPerdu implements Initializable {

    @FXML
    private TableColumn<Objet, String> lieuCol;
    @FXML
    private AnchorPane recpane;
    @FXML
    private TableColumn<Objet, String> descriptionCol;
    @FXML
    private TextField descriptionTf;
    @FXML
    private DatePicker dateTf;
    @FXML
    private TextField idTf;
    @FXML
    private TextField filterField;
    @FXML
    private TableColumn<Objet, String> nomCol;
    @FXML
    private TableColumn<Objet, Integer> idCol;
    @FXML
    private TextField nomTf;
    @FXML
    private TableView<Objet> recTab;
    @FXML
    private TableColumn<Objet, String> statusCol;
    @FXML
    private TableColumn<Objet, String> dateCol;
    @FXML
    private TableColumn<Objet, String> categorieCol;
    @FXML
    private TextField categorieTf;
    @FXML
    private TextField imagePathTf;

    private ObservableList<Objet> objetList = FXCollections.observableArrayList();
    private Connection connection;
    private ObjetPerduService service;
    private CloudinaryService cloudinaryService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = MyConnection.getInstance().getCnx();
        service = new ObjetPerduService();
        cloudinaryService = new CloudinaryService();
        initializeTable();
        loadObjets();
        recherche();
    }

    private void initializeTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        lieuCol.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        categorieCol.setCellValueFactory(new PropertyValueFactory<>("categorie"));
    }

    private void loadObjets() {
        objetList.clear();
        objetList.addAll(service.afficherObjets());
        recTab.setItems(objetList);
    }

    @FXML
    void addObjet(ActionEvent event) {
        if (controlSaisie()) {
            String nom = nomTf.getText();
            String status = "Pending"; // Default status
            String date = dateTf.getValue().toString();
            String description = descriptionTf.getText();
            String lieu = descriptionTf.getText(); // Using descriptionTf for lieu
            String categorie = categorieTf.getText();
            String imagePath = imagePathTf.getText();

            Objet objet = new Objet(nom, status, date, description, lieu, categorie, imagePath);
            service.ajouterObjet(objet);
            showAlert("Success", "Objet added successfully!");
            clearFields();
            loadObjets();
        }
    }

    @FXML
    void deleteObjet(ActionEvent event) {
        Objet selected = recTab.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.supprimerObjet(selected.getId());
            showAlert("Success", "Objet deleted successfully!");
            loadObjets();
        } else {
            showAlert("Error", "Please select an objet to delete!");
        }
    }

    @FXML
    void updateObjet(ActionEvent event) {
        if (controlSaisie()) {
            try {
                int id = Integer.parseInt(idTf.getText());
                String nom = nomTf.getText();
                String status = "Pending"; // Default or retrieve from UI if needed
                String date = dateTf.getValue().toString();
                String description = descriptionTf.getText();
                String lieu = descriptionTf.getText();
                String categorie = categorieTf.getText();
                String imagePath = imagePathTf.getText();

                Objet objet = new Objet(id, nom, status, date, description, lieu, categorie, imagePath);
                service.modifierObjet(id, objet);
                showAlert("Success", "Objet updated successfully!");
                clearFields();
                loadObjets();
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid ID format!");
            }
        }
    }

    @FXML
    void find(ActionEvent event) {
        try {
            String objectId = idTf.getText().trim();
            if (objectId.isEmpty()) {
                showNotification("Erreur", "Veuillez entrer un ID valide.", NotificationType.ERROR);
                return;
            }

            int id = Integer.parseInt(objectId);
            int rowsUpdated = service.updateEtat(id, "trouvé");

            if (rowsUpdated > 0) {
                clearFields();
                loadObjets();
                showNotification("Succès", "Objet marqué comme trouvé !", NotificationType.SUCCESS);
            } else {
                showNotification("Attention", "Aucun objet trouvé avec cet ID.", NotificationType.WARNING);
            }
        } catch (NumberFormatException e) {
            showNotification("Erreur", "ID doit être un nombre.", NotificationType.ERROR);
        } catch (SQLException e) {
            showNotification("Erreur", "Une erreur s'est produite: " + e.getMessage(), NotificationType.ERROR);
        }
    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", ".png", ".jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String imageUrl = cloudinaryService.uploadImage(selectedFile);

            if (imageUrl != null) {
                imagePathTf.setText(imageUrl);
                showAlert("Success", "Image uploaded successfully!\nURL: " + imageUrl);
            } else {
                showAlert("Error", "Image upload failed.");
            }
        }
    }

    @FXML
    void NavRecompense(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Consultation");
            Parent root = FXMLLoader.load(getClass().getResource("/Recompense.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ObjetPerdu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void CHAT(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chatbot.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Chatbot");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            Logger.getLogger(ObjetPerdu.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void getSelected(javafx.scene.input.MouseEvent event) {
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
            }
        }
    }

    @FXML
    public void recherche() {
        String filter = filterField.getText().toLowerCase();
        if (filter.isEmpty()) {
            recTab.setItems(objetList);
        } else {
            ObservableList<Objet> filteredData = FXCollections.observableArrayList();
            for (Objet item : objetList) {
                if (item.getNom().toLowerCase().contains(filter) ||
                        item.getStatus().toLowerCase().contains(filter) ||
                        item.getCategorie().toLowerCase().contains(filter)) {
                    filteredData.add(item);
                }
            }
            recTab.setItems(filteredData);
        }
    }

    private boolean controlSaisie() {
        if (dateTf.getValue() == null) {
            showAlert("Erreur", "Date ne peut pas être vide.");
            return false;
        }
        if (nomTf.getText().trim().isEmpty()) {
            showAlert("Erreur", "Nom ne peut pas être vide.");
            return false;
        }
        if (descriptionTf.getText().trim().isEmpty()) {
            showAlert("Erreur", "Description ne peut pas être vide.");
            return false;
        }
        if (categorieTf.getText().trim().isEmpty()) {
            showAlert("Erreur", "Catégorie ne peut pas être vide.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showNotification(String title, String message, NotificationType type) {
        Notifications notification = Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(4))
                .position(Pos.TOP_RIGHT)
                .onAction(e -> System.out.println("Notification clicked!"));
        notification.show();
    }

    private void clearFields() {
        idTf.clear();
        nomTf.clear();
        descriptionTf.clear();
        categorieTf.clear();
        imagePathTf.clear();
        dateTf.setValue(null);
    }

    enum NotificationType {
        SUCCESS, ERROR, WARNING
    }
}