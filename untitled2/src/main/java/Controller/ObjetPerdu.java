package Controller;

import Services.ObjetPerduService;
import entites.Objet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tools.MyConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjetPerdu implements Initializable {

    @FXML
    private TableColumn<?, ?> User;

    @FXML
    private TableColumn<?, ?> description;

    @FXML
    private TextField desctf;
    @FXML
    private DatePicker datetf;
    @FXML
    private TextField idtf;

    @FXML
    private TextField filterField;

    @FXML
    private TableColumn<?, ?> nom;

    @FXML
    private TableColumn<?, ?> idcol;

    @FXML
    private TextField nomtf;

    @FXML
    private TableView<Objet> recTab;

    @FXML
    private TableColumn<?, ?> status;
    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private TextField usertf;
    private ObservableList<Objet> destinationList = FXCollections.observableArrayList();
    private Connection connection;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = MyConnection.getInstance().getCnx();
        initializeTable();
        loadDestinations();
        setupSearchFilter();
    }

    private void initializeTable() {
        User.setCellValueFactory(new PropertyValueFactory<>("idP"));
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        description.setCellValueFactory(new PropertyValueFactory<>("lieuP"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadDestinations() {
        destinationList.clear();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM objets_perdu");
            while (rs.next()) {
                destinationList.add(new Objet( rs.getInt("id"),rs.getString("nom"),rs.getInt("idP"), rs.getString("lieuP"),  rs.getString("date"), rs.getString("status")));
            }
        } catch (SQLException e) {
            Logger.getLogger(ObjetPerdu.class.getName()).log(Level.SEVERE, null, e);
        }
        recTab.setItems(destinationList);
    }

    @FXML
    void addDestination(ActionEvent event) throws SQLException {
        String name = nomtf.getText();
        String description = desctf.getText();
        String user = usertf.getText();
        String date = datetf.getValue().toString();
        String status = "Pending"; // Default status

        if (name.isEmpty() || description.isEmpty() || user.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        ObjetPerduService service = new ObjetPerduService();
        Objet destination = new Objet( name,1, description, date ,status );
        service.ajouterObjet(destination);
        showAlert("Success", "Objet added successfully!");
        loadDestinations();
    }

    @FXML
    void deleteDestination(ActionEvent event) throws SQLException {
        Objet selected = recTab.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ObjetPerduService service = new ObjetPerduService();

            service.supprimerObjet(selected.getId());
            showAlert("Success", "Objet deleted successfully!");
            loadDestinations();
        } else {
            showAlert("Error", "Please select a destination to delete!");
        }
    }

    @FXML
    public void recherche() throws SQLException {
        ObjetPerduService ff= new ObjetPerduService();
        FilteredList<Objet> filteredData = new FilteredList<>(destinationList , b -> true);
        Objet r = new Objet();
        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(objet -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (objet.getNom().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(r.getNom()).indexOf(lowerCaseFilter)!=-1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Objet> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(recTab.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        recTab.setItems(sortedData);
        System.out.println(sortedData);


    }

    private void setupSearchFilter() {
        FilteredList<Objet> filteredData = new FilteredList<>(destinationList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(destination -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return destination.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        destination.getLieuP().toLowerCase().contains(lowerCaseFilter) ;
            });
        });

        SortedList<Objet> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(recTab.comparatorProperty());
        recTab.setItems(sortedData);
    }

    private void showAlert(String title, String message) {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private Connection cnx = null;
    private PreparedStatement pst = null ;
    private ResultSet rs = null ;
    @FXML
    void updateDestination(ActionEvent event) {
        try {
            cnx = MyConnection.getInstance().getCnx();
            String value0 = idtf.getText();
            String value1 = nomtf.getText();

            String value2 = usertf.getText();
            String value4 = datetf.getValue().toString();

System.out.println("hello");
            String sql = "update objets_perdu set nom= '"+value1+"',lieuP= '"+value2+"',date= '"+value4+"' where id='"+value0+"' ";
            pst= cnx.prepareStatement(sql);
            pst.execute();
            System.out.println(sql);

            nomtf.setText("");

            idtf.setText("");

            desctf.setText("");
            loadDestinations();


        } catch ( SQLException e) {
        }
    }
    @FXML
    void approveDestination(ActionEvent event) {
        try {
            cnx = MyConnection.getInstance().getCnx();
            String value0 = idtf.getText();

            String value1 = "Approved";

            String sql = "update destination set status='"+value1+"'  where id='"+value0+"' ";
            System.out.println(sql);

            pst= cnx.prepareStatement(sql);
            pst.execute();

            nomtf.setText("");

            idtf.setText("");

            desctf.setText("");
            loadDestinations();


        } catch ( SQLException e) {
        }
    }
    public Objet gettempReclamation(TableColumn.CellEditEvent edittedCell) {
        Objet test = recTab.getSelectionModel().getSelectedItem();
        return test;
    }
    @FXML
    private void getSelected(javafx.scene.input.MouseEvent event) {
        int index = recTab.getSelectionModel().getSelectedIndex();
        if (index <= -1){

            return;
        }
        idtf.setText(idcol.getCellData(index).toString());
        nomtf.setText(nom.getCellData(index).toString());
        usertf.setText(description.getCellData(index).toString());




    }
}