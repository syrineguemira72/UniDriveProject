package edu.unidrive.controllers;

import edu.unidrive.entities.Recompense;
import edu.unidrive.services.RecompenseService;
import edu.unidrive.tools.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecompenseController implements Initializable {

    @FXML
    private ComboBox<String> combo;


    @FXML
    private TextField filterField;
    @FXML
    private TextField usertf;

    @FXML
    private TableColumn<?, ?> id;

    @FXML
    private TableColumn<?, ?> idT;

    @FXML
    private TableColumn<?, ?> idcol;
    @FXML
    private AnchorPane recpane;
    @FXML
    private TextField idtf;

    @FXML
    private TableColumn<?, ?> objp;


    @FXML
    private TableColumn<?, ?> reduction;

    @FXML
    private TextField reductiontf;

    @FXML
    private TableView<Recompense> recTab;
    private ObservableList<Recompense> List = FXCollections.observableArrayList();
    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("hello");
        connection = MyConnection.getInstance().getCnx();
        initializeTable();
        loadBillets();
        Connection cnxc = MyConnection.getInstance().getCnx();
        try {
            rs = cnxc.createStatement().executeQuery("SELECT nom FROM objets_perdu where status='trouvé' ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(true)

        // Now add the comboBox addAll statement
        {

            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            try {
                combo.getItems().addAll(rs.getString("nom"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        //  setupSearchFilter();
    }

    private void initializeTable() {
        objp.setCellValueFactory(new PropertyValueFactory<>("idOP"));
        idT.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        reduction.setCellValueFactory(new PropertyValueFactory<>("reduction"));
         idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void loadBillets() {
        List.clear();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM recompense");
            while (rs.next()) {
                List.add(new Recompense(rs.getInt("id"),rs.getInt("reduction"), rs.getInt("idOP"), rs.getInt("idUser")));
            }
        } catch (SQLException e) {
            Logger.getLogger(RecompenseController.class.getName()).log(Level.SEVERE, null, e);
        }
        recTab.setItems(List);
    }

    public boolean controlSaisie() {
        // Vérification de l'entier (par exemple, le champ pour la quantité ou un autre champ)
        try {
            double entier = Double.parseDouble(reductiontf.getText());
            if (entier <= 0) {
                showAlert("Erreur", "L'entier doit être positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le format de recompense est invalide.");
            return false;
        }


        // Vérification de la chaîne de caractères (par exemple, le champ pour le nom ou autre)

        if (!combo.getValue()) {  // If false, show error
            showAlert("Erreur", "Objet perdu ne peut pas être vide.");
            return false;
        }
        return true;

    }


    @FXML
    void addRecompense(ActionEvent event) throws SQLException{
        int value4=0;
        int value5=0;
        Connection cnx = MyConnection.getInstance().getCnx();
        rs = cnx.createStatement().executeQuery("SELECT id,idT FROM objets_perdu where nom ='"+ combo.getValue() +"' ");
        while(rs.next()) {
            value4 = rs.getInt("id");

            value5 = rs.getInt("idT");
            // Now add the comboBox addAll statement

        }

        if(controlSaisie()) {
            Recompense a = new Recompense(Double.parseDouble(reductiontf.getText()), value4,value5 );
            RecompenseService bs = new RecompenseService();

            bs.ajouterRecompense(a);
            showAlert("Success", "Ticket added successfully!");

        }

        loadBillets();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void deleteRecompense(ActionEvent event) throws SQLException {
        Recompense selected = recTab.getSelectionModel().getSelectedItem();
        if (selected != null) {
            RecompenseService service = new RecompenseService();

            service.supprimerRecompense(selected.getId());
            showAlert("Success", "Recompense deleted successfully!");
            loadBillets();
        } else {
            showAlert("Error", "Please select a destination to delete!");
        }
    }


    @FXML
    private void getSelected(javafx.scene.input.MouseEvent event) {
        int index = recTab.getSelectionModel().getSelectedIndex();
        if (index <= -1){

            return;
        }
        System.out.println(idcol.getCellData(index).toString());
        idtf.setText(idcol.getCellData(index).toString());
        reductiontf.setText(reduction.getCellData(index).toString());


    }

        @FXML
        public void recherche(){
            if (filterField == null || filterField.getText().isEmpty()) {
                recTab.setItems(List);  // Show all data if no filter
            } else {
                ObservableList<Recompense> filteredData = FXCollections.observableArrayList();
                for (Recompense item : List) {
                    if (String.valueOf(item.getReduction()).toLowerCase().contains(filterField.getText().toLowerCase()) ||
                            String.valueOf(item.getIdOP()).toLowerCase().contains(filterField.getText().toLowerCase())) {
                        filteredData.add(item);
                    }
                }
                recTab.setItems(filteredData);
            }

        }

    private Stage primaryStage;

    @FXML
    private void printRecompense(ActionEvent event) {

        System.out.println("To Printer!");
        PrinterJob job = PrinterJob.createPrinterJob();
        if(job != null){
            Window primaryStage = null;
            job.showPrintDialog(this.primaryStage);

            Node root = this.recTab;
            job.printPage(root);
            job.endJob();
        }

    }
    private Connection cnx = null;
    private PreparedStatement pst = null ;
    private ResultSet rs = null ;
    @FXML
    void loadNom(ActionEvent event) throws SQLException {
        int value4 = 0 ;

        cnx = MyConnection.getInstance().getCnx();

        Connection cnx = MyConnection.getInstance().getCnx();
        rs = cnx.createStatement().executeQuery("SELECT id FROM objets_perdu where nom='"+ combo.getValue() +"'");
        while(rs.next())
            value4 = rs.getInt("id");
        rs = cnx.createStatement().executeQuery("SELECT nom FROM user where id='"+value4+"'");
        while(rs.next())
            usertf.setText(String.valueOf(rs.getInt("id")));

    }
    @FXML
    void NavRecompense(ActionEvent event) throws IOException {
        AnchorPane newvalscene = (AnchorPane) FXMLLoader.load(getClass().getResource("/ObjetPerdu.fxml"));
        recpane.getChildren().setAll(newvalscene.getChildren());
        recpane = new AnchorPane();

    }
    @FXML
    boolean updateRecompense(ActionEvent event) {
        int value4 = 0 ;
        int value5 = 0 ;
        try {

            cnx = MyConnection.getInstance().getCnx();
            String value0 = idtf.getText();

            String value1 = reductiontf.getText();



            if(controlSaisie()){
                Connection cnx = MyConnection.getInstance().getCnx();
                String sql = "SELECT id, idT FROM objets_perdu WHERE nom=?";
                try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
                    pstmt.setString(1, String.valueOf(combo.getValue()));
                    rs = pstmt.executeQuery();
                    // Process results...
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
                while(rs.next()) {
                    value4 = rs.getInt("id");

                    value5 = rs.getInt("id");
                }
                sql = "UPDATE recompense SET idOP=?, idUser=?, reductionn=? WHERE id=?";
                try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
                    // Set parameters in order (1-based index)
                    pstmt.setInt(1, Integer.parseInt(String.valueOf(value4)));    // Assuming idOP is numeric
                    pstmt.setInt(2, Integer.parseInt(String.valueOf(value5)));    // Assuming idUser is numeric
                    pstmt.setString(3, value1);                  // Assuming reductionn is string
                    pstmt.setInt(4, Integer.parseInt(value0));   // Assuming id is numeric

                    int rowsAffected = pstmt.executeUpdate();
                    return rowsAffected > 0;  // Returns true if update was successful
                } catch (SQLException | NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }


            }

            combo.setValue(null);
            loadBillets();


        } catch ( SQLException e) {
        }
        return false;
    }
}
