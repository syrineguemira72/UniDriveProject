package controller;

import entite.Recompence;
import entite.Objet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.RecompenceService;
import service.ObjetService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecompenceController {
    @FXML private TableView<Recompence> recTab;
    @FXML private TableColumn<Recompence, Integer> idCol;
    @FXML private TableColumn<Recompence, String> objetCol;
    @FXML private TableColumn<Recompence, String> userCol; // Will display username
    @FXML private TableColumn<Recompence, Double> reductionCol;

    @FXML private TextField reductionTf, userTf, idTf, filterField;
    @FXML private ComboBox<String> objetCombo;

    private RecompenceService recompenceService;
    private ObjetService objetService;
    private ObservableList<Recompence> recompenceList = FXCollections.observableArrayList();
    private Map<String, Integer> objetNameToIdMap = new HashMap<>();

    @FXML
    public void initialize() {
        try {
            recompenceService = new RecompenceService();
            objetService = new ObjetService();
            configureTableColumns();
            loadData();
            loadObjetNames();
        } catch (SQLException e) {
            showAlert("Erreur", "Connexion BD échouée: " + e.getMessage());
        }
    }

    private void configureTableColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        objetCol.setCellValueFactory(cellData -> {
            try {
                String nomObjet = recompenceService.getObjetName(cellData.getValue().getObjectPerduId());
                return new javafx.beans.property.SimpleStringProperty(nomObjet);
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("Erreur");
            }
        });
        userCol.setCellValueFactory(cellData -> {
            try {
                String username = recompenceService.getUtilisateurName(cellData.getValue().getUtilisateurId());
                return new javafx.beans.property.SimpleStringProperty(username);
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("Inconnu");
            }
        });
        reductionCol.setCellValueFactory(new PropertyValueFactory<>("reduction"));
    }

    private void loadData() {
        try {
            recompenceList.clear();
            List<Recompence> recompences = recompenceService.getAllRecompences();
            if (recompences.isEmpty()) {
                showAlert("Info", "Aucune récompense trouvée dans la base de données.");
            } else {
                recompenceList.addAll(recompences);
                recTab.setItems(recompenceList);
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Échec chargement données: " + e.getMessage());
        }
    }

    private void loadObjetNames() {
        objetCombo.getItems().clear();
        objetNameToIdMap.clear();
        List<Objet> objets;
        try {
            objets = objetService.getAllObjets();
            if (objets.isEmpty()) {
                showAlert("Info", "Aucun objet trouvé dans la base de données.");
            }
            for (Objet objet : objets) {
                objetCombo.getItems().add(objet.getNom());
                objetNameToIdMap.put(objet.getNom(), objet.getId());
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Échec chargement objets: " + e.getMessage());
        }
    }

    @FXML
    private void getSelected(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Recompence selected = recTab.getSelectionModel().getSelectedItem();
            if (selected != null) {
                idTf.setText(String.valueOf(selected.getId()));
                try {
                    userTf.setText(recompenceService.getUtilisateurName(selected.getUtilisateurId()));
                } catch (SQLException e) {
                    userTf.setText("Inconnu");
                }
                reductionTf.setText(String.valueOf(selected.getReduction()));
                try {
                    String nomObjet = recompenceService.getObjetName(selected.getObjectPerduId());
                    objetCombo.setValue(nomObjet);
                } catch (SQLException e) {
                    showAlert("Erreur", "Impossible de charger l'objet");
                }
            }
        }
    }

    @FXML
    private void addRecompence() {
        try {
            if (objetCombo.getValue() == null || userTf.getText().isEmpty() || reductionTf.getText().isEmpty()) {
                showAlert("Erreur", "Tous les champs sont requis");
                return;
            }
            Integer objetId = objetNameToIdMap.get(objetCombo.getValue());
            if (objetId == null) {
                showAlert("Erreur", "Objet invalide");
                return;
            }
            // Assuming userTf contains a username, we need to map it to utilisateur_id (requires UtilisateurService)
            // For now, this is a placeholder; you'll need to implement user ID lookup
            int utilisateurId = 1; // Replace with actual logic to get utilisateur_id
            Recompence recompence = new Recompence();
            recompence.setObjectPerduId(objetId);
            recompence.setUtilisateurId(utilisateurId);
            recompence.setReduction(Double.parseDouble(reductionTf.getText()));
            recompenceService.addRecompence(recompence);
            loadData();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La réduction doit être un nombre");
        } catch (SQLException e) {
            showAlert("Erreur BD", "Échec ajout: " + e.getMessage());
        }
    }

    @FXML
    private void updateRecompence() {
        try {
            if (idTf.getText().isEmpty() || objetCombo.getValue() == null || userTf.getText().isEmpty() || reductionTf.getText().isEmpty()) {
                showAlert("Erreur", "Tous les champs sont requis");
                return;
            }
            Integer objetId = objetNameToIdMap.get(objetCombo.getValue());
            if (objetId == null) {
                showAlert("Erreur", "Objet invalide");
                return;
            }
            int utilisateurId = 1; // Replace with actual logic to get utilisateur_id
            Recompence recompence = new Recompence();
            recompence.setId(Integer.parseInt(idTf.getText()));
            recompence.setObjectPerduId(objetId);
            recompence.setUtilisateurId(utilisateurId);
            recompence.setReduction(Double.parseDouble(reductionTf.getText()));
            recompenceService.updateRecompence(recompence);
            loadData();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID et réduction doivent être des nombres");
        } catch (SQLException e) {
            showAlert("Erreur BD", "Échec mise à jour: " + e.getMessage());
        }
    }

    @FXML
    private void deleteRecompence() {
        try {
            if (idTf.getText().isEmpty()) {
                showAlert("Erreur", "Sélectionnez un élément");
                return;
            }
            int id = Integer.parseInt(idTf.getText());
            recompenceService.deleteRecompence(id);
            loadData();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID invalide");
        } catch (SQLException e) {
            showAlert("Erreur BD", "Échec suppression: " + e.getMessage());
        }
    }

    @FXML
    private void recherche() {
        try {
            String keyword = filterField.getText();
            recompenceList.clear();
            if (keyword.isEmpty()) {
                recompenceList.addAll(recompenceService.getAllRecompences());
            } else {
                recompenceList.addAll(recompenceService.searchRecompences(keyword));
            }
            recTab.setItems(recompenceList);
        } catch (SQLException e) {
            showAlert("Erreur", "Échec recherche: " + e.getMessage());
        }
    }

    @FXML
    private void navRecompence() {
        try {
            URL resource = getClass().getResource("/ObjetPerdu.fxml");
            if (resource == null) {
                showAlert("Erreur", "Fichier ObjetPerdu.fxml non trouvé dans /fxml/");
                return;
            }
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Scene scene = new Scene(fxmlLoader.load(), 800, 500);
            Stage stage = new Stage();
            stage.setTitle("Objets Perdus");
            stage.setScene(scene);
            stage.show();
            ((Stage) recTab.getScene().getWindow()).close();
        } catch (IOException e) {
            showAlert("Erreur", "Échec chargement vue ObjetPerdu.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void loadNom() {
        // Empty method to satisfy FXML onAction
    }

    @FXML
    private void printRecompence() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("RecompenceReport.pdf"));
            document.open();
            document.add(new Paragraph("Recompence Report - " + java.time.LocalDate.now()));
            document.add(new Paragraph("------------------------"));
            for (Recompence r : recompenceList) {
                String objetName = recompenceService.getObjetName(r.getObjectPerduId());
                String username = "Inconnu"; // Placeholder
                try {
                    username = recompenceService.getUtilisateurName(r.getUtilisateurId());
                } catch (SQLException e) {
                    // Handle exception
                }
                document.add(new Paragraph(
                        "ID: " + r.getId() +
                                ", Objet: " + objetName +
                                ", Utilisateur: " + username +
                                ", Reduction: " + r.getReduction()
                ));
            }
            document.close();
            showAlert("Succès", "Rapport PDF généré: RecompenceReport.pdf");
        } catch (DocumentException | IOException | SQLException e) {
            showAlert("Erreur", "Échec génération PDF: " + e.getMessage());
        }
    }

    private void clearFields() {
        idTf.clear();
        userTf.clear();
        reductionTf.clear();
        objetCombo.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.equals("Erreur")) {
            alert.setAlertType(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}