/*package gestion_aide.controller;

import gestion_aide.entities.aide;
import gestion_aide.services.AideService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class AideController {

    @FXML
    private TableView<aide> aideTable;  // TableView to display aides

    @FXML
    private TableColumn<aide, Integer> idColumn;  // Column for ID
    @FXML
    private TableColumn<aide, String> typeColumn;  // Column for Nom
    @FXML
    private TableColumn<aide, String> descriptionColumn;  // Column for Prenom

    private AideService aideService;  // Service class for interacting with the database

    public AideController() {
        aideService = new AideService();
    }

    // Method to initialize the TableView with data
    @FXML
    private void initialize() {
        // Set up the columns to match the properties of the aide class
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        // Load data from the database into the TableView
        loadData();
    }

    // Method to load data from the database and bind to the TableView
    private void loadData() {
        List<aide> aides = aideService.getallData();  // Get all data from the database
        ObservableList<aide> aideList = FXCollections.observableArrayList(aides);  // Convert list to ObservableList
        aideTable.setItems(aideList);  // Set the data in the TableView
    }
}
*/