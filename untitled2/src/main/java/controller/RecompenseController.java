package controller;

import Services.RecompenseService;
import entites.Recompense;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import tools.MyConnection;

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
    private ObservableList<Recompense> billetList = FXCollections.observableArrayList();
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
        id.setCellValueFactory(new PropertyValueFactory<>("idT"));
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
    }


    private void loadBillets() {
        billetList.clear();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM recompense");
            while (rs.next()) {
                billetList.add(new Recompense(rs.getInt("id"),rs.getInt("reduction"), rs.getInt("idOP"), rs.getInt("idUser")));
            }
        } catch (SQLException e) {
            Logger.getLogger(RecompenseController.class.getName()).log(Level.SEVERE, null, e);
        }
        recTab.setItems(billetList);
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




        if (combo.getValue()==null){


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
        rs = cnx.createStatement().executeQuery("SELECT id,idT FROM objets_perdu where nom ='"+combo.getValue().toString()+"' ");
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
    void recherche(KeyEvent event) {

    }
    private Connection cnx = null;
    private PreparedStatement pst = null ;
    private ResultSet rs = null ;
    @FXML
    void loadNom(ActionEvent event) throws SQLException {
        int value4 = 0 ;

        cnx = MyConnection.getInstance().getCnx();

        Connection cnx = MyConnection.getInstance().getCnx();
        rs = cnx.createStatement().executeQuery("SELECT id FROM objets_perdu where nom='"+combo.getValue().toString()+"'");
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
    void updateRecompense(ActionEvent event) {
        int value4 = 0 ;
        int value5 = 0 ;
        try {

            cnx = MyConnection.getInstance().getCnx();
            String value0 = idtf.getText();

            String value1 = reductiontf.getText();



            if(controlSaisie()){
                Connection cnx = MyConnection.getInstance().getCnx();
                rs = cnx.createStatement().executeQuery("SELECT id,idT FROM objets_perdu where nom='"+combo.getValue().toString()+"'");
                while(rs.next()) {
                    value4 = rs.getInt("id");

                    value5 = rs.getInt("id");
                }
                String sql = "update recompense set idOP= '"+value4+"',idUser= '"+value5+"',reductionn='"+value1+"' where id='"+value0+"' ";
                pst= cnx.prepareStatement(sql);
                System.out.println(sql);

                pst.execute();


                idtf.setText("");
 reductiontf.setText("");
                showAlert("Success", "recompense updated successfully!");
            }

            combo.setValue(null);
            loadBillets();


        } catch ( SQLException e) {
        }
    }
}
