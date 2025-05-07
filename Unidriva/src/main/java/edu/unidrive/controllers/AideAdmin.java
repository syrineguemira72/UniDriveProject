package edu.unidrive.controllers;

import edu.unidrive.entities.aide;
import edu.unidrive.entities.Association;
import edu.unidrive.services.AideService;
import edu.unidrive.services.AssociationService;
import edu.unidrive.tools.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AideAdmin {

    @FXML private TextField searchField;
    @FXML private TextField currencyField;
    @FXML private TextField descriptionField;
    @FXML private TextField montantField;
    @FXML private ChoiceBox<Association> associationChoiceBox;   // Now holds Association objects

    @FXML private TableView<aide> aideTable;
    @FXML private TableColumn<aide, Integer> idColumn;
    @FXML private TableColumn<aide, String> currencyColumn;
    @FXML private TableColumn<aide, String> descriptionColumn;
    @FXML private TableColumn<aide, String> montantColumn;
    @FXML private TableColumn<aide, String> createdAtColumn;
    @FXML private TableColumn<aide, String> associationColumn;   // Shows association name

    @FXML private LineChart<String, Number> montantChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    private AideService aideService;
    private AssociationService associationService;

    public AideAdmin() {
        aideService = new AideService();
        associationService = new AssociationService();
    }

    @FXML
    private void initialize() {
        // Configure Table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        currencyColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        // association name from FK
        associationColumn.setCellValueFactory(cell -> {
            int assocId = cell.getValue().getAssociationId();
            Association assoc = associationService.getEntity(assocId);
            String name = assoc != null ? assoc.getNom() : "";
            return new SimpleStringProperty(name);
        });

        // Populate choice box with associations
        List<Association> associations = associationService.getallData();
        associationChoiceBox.setItems(FXCollections.observableArrayList(associations));
        associationChoiceBox.setConverter(new StringConverter<Association>() {
            @Override public String toString(Association assoc) { return assoc != null ? assoc.getNom() : ""; }
            @Override public Association fromString(String string) { return null; }
        });

        loadData();
        loadMontantPerDayData();

        // Selection listener to populate fields
        aideTable.getSelectionModel().selectedItemProperty().addListener((obs, oldA, newA) -> {
            if (newA != null) populateFields(newA);
        });

        // Filter & sort
        FilteredList<aide> filtered = new FilteredList<>(aideTable.getItems(), p -> true);
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            String lower = newV.toLowerCase();
            filtered.setPredicate(a -> {
                if (newV.isEmpty()) return true;
                return a.getCurrency().toLowerCase().contains(lower)
                        || a.getDescription().toLowerCase().contains(lower)
                        || a.getMontant().toLowerCase().contains(lower)
                        || associationService.getEntity(a.getAssociationId()).getNom().toLowerCase().contains(lower);
            });
        });
        SortedList<aide> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(aideTable.comparatorProperty());
        aideTable.setItems(sorted);
    }

    private void loadMontantPerDayData() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Montant Per Day");

        // Grab the singleton connection—but do NOT wrap it in try‑with‑resources
        Connection conn = MyConnection.getInstance().getCnx();
        String sql = "SELECT DATE(created_at) AS day, SUM(montant) AS total FROM aide GROUP BY day ORDER BY day";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                        rs.getDate("day").toString(),
                        rs.getDouble("total")
                ));
            }
            montantChart.getData().add(series);

        } catch (SQLException e) {
            showAlert("Erreur graphique", e.getMessage(), Alert.AlertType.ERROR);
        }
        // Do NOT close conn here; it remains open for other DAO operations
    }


    private void loadData() {
        ObservableList<aide> data = FXCollections.observableArrayList(aideService.getallData());
        aideTable.setItems(data);
    }

    private void populateFields(aide a) {
        currencyField.setText(a.getCurrency());
        descriptionField.setText(a.getDescription());
        montantField.setText(a.getMontant());
        // select the corresponding association in the choice box
        associationChoiceBox.getSelectionModel().select(
                associationService.getEntity(a.getAssociationId())
        );
    }

    @FXML
    void ajouterAideaction(ActionEvent event) {
        if (currencyField.getText().isEmpty() || descriptionField.getText().isEmpty()
                || montantField.getText().isEmpty() || associationChoiceBox.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.INFORMATION);
            return;
        }
        try {
            double m = Double.parseDouble(montantField.getText());
            if (m <= 0) throw new NumberFormatException();
            int assocId = associationChoiceBox.getValue().getId();

            aide newA = new aide(currencyField.getText(), descriptionField.getText(), montantField.getText(),
                    LocalDate.now().toString(), 0, assocId);
            aideService.addEntity(newA);
            loadData();
            showAlert("Succès","Aide ajoutée.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur","Montant invalide.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void deleteSelectedRow(ActionEvent event) {
        aide sel = aideTable.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Erreur","Sélectionnez une aide.",Alert.AlertType.ERROR); return; }
        Optional<ButtonType> r = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer?", ButtonType.OK, ButtonType.CANCEL).showAndWait();
        if (r.isPresent() && r.get() == ButtonType.OK) {
            aideService.deleteEntity(sel);
            loadData();
            showAlert("Succès","Supprimé.",Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void updateAction(ActionEvent event) {
        aide sel = aideTable.getSelectionModel().getSelectedItem();
        if (sel == null || currencyField.getText().isEmpty() || descriptionField.getText().isEmpty()
                || montantField.getText().isEmpty() || associationChoiceBox.getValue() == null) {
            showAlert("Erreur","Sélection et tous les champs requis.",Alert.AlertType.ERROR);
            return;
        }
        try {
            double m = Double.parseDouble(montantField.getText()); if (m <= 0) throw new NumberFormatException();
            int assocId = associationChoiceBox.getValue().getId();
            aide upd = new aide(sel.getId(), currencyField.getText(), descriptionField.getText(), montantField.getText(),
                    LocalDate.now().toString(), sel.getIdUser(), assocId);
            aideService.updateEntity(sel.getId(), upd);
            loadData();
            showAlert("Succès","Mis à jour.",Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur","Montant invalide.",Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    @FXML void goToAnotherPage(ActionEvent e) { navigate(e, "/AssociationAdminPage.fxml"); }
    @FXML void goToBack(ActionEvent e) { navigate(e, "/CRUD.fxml"); }

    private void navigate(ActionEvent e, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage st = (Stage)((Node)e.getSource()).getScene().getWindow();
            st.setScene(new Scene(root)); st.show();
        } catch (IOException ex) {
            showAlert("Navigation error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
