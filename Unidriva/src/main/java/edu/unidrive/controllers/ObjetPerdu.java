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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjetPerdu implements Initializable{

    @FXML
    private TableColumn<?, ?> User;
    @FXML
    private AnchorPane recpane;


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
    void NavRecompense(ActionEvent event) throws IOException {


        try {
            Stage stage = new Stage();
            stage.setTitle("Consultation");
            Parent root = FXMLLoader.load(getClass().getResource("/Recompense.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }


    @FXML
    private TextField usertf;
    private ObservableList<Objet> destinationList = FXCollections.observableArrayList();
    private Connection connection;

    public boolean controlSaisie() {
    // Vérification de la chaîne de caractères (par exemple, le champ pour le nom ou autre)
        if (datetf.getValue()==null){
            showAlert("Erreur", "Date ne peut pas être vide.");
            return false;
        }
        if (nomtf.getText()==null || nomtf.getText().trim().isEmpty()){
            showAlert("Erreur", "nom ne peut pas être vide.");
            return false;
        }
        if (usertf.getText()==null || usertf.getText().trim().isEmpty()){
            showAlert("Erreur", "Lieu ne peut pas être vide.");
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = MyConnection.getInstance().getCnx();
        initializeTable();
        loadDestinations();
        recherche();
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
       if(controlSaisie()) {
           String name = nomtf.getText();
           String description = usertf.getText();
           String user = usertf.getText();
           String date = datetf.getValue().toString();
           String status = "Pending"; // Default status

           if (name.isEmpty() || description.isEmpty() || date.isEmpty()) {
               showAlert("Error", "All fields must be filled!");
               return;
           }

           ObjetPerduService service = new ObjetPerduService();
           Objet destination = new Objet(name, 1, description, date, status);
           service.ajouterObjet(destination);
           showAlert("Success", "Objet added successfully!");
           loadDestinations();
       }
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
            showAlert("Error", "Please select an object to delete!");
        }
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
    void find(ActionEvent event) {
        try {
            // Get the database connection
            cnx = MyConnection.getInstance().getCnx();

            // Get user input
            String objectId = idtf.getText().trim();
            String newStatus = "trouvé";

            if (objectId.isEmpty()) {
                showNotification("Erreur", "Veuillez entrer un ID valide.", NotificationType.ERROR);
                return;
            }

            // Prepare the SQL query with a parameter
            String sql = "UPDATE objets_perdu SET status = ? WHERE id = ?";
            pst = cnx.prepareStatement(sql);
            pst.setString(1, newStatus);
            pst.setString(2, objectId);

            // Execute the update
            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                // Clear fields
                idtf.clear();
                nomtf.clear();
                desctf.clear();

                // Reload data in the table
                loadDestinations();

                // Show success notification
                showNotification("Succès", "Objet marqué comme trouvé !", NotificationType.SUCCESS);
            } else {
                showNotification("Attention", "Aucun objet trouvé avec cet ID.", NotificationType.WARNING);
            }
        } catch (SQLException e) {
            showNotification("Erreur", "Une erreur s'est produite: " + e.getMessage(), NotificationType.ERROR);
            e.printStackTrace();  // Log the error
        }
    }

    public Objet gettempReclamation(TableColumn.CellEditEvent edittedCell) {
        Objet test = recTab.getSelectionModel().getSelectedItem();
        return test;
    }

    private void showNotification(String title, String message, NotificationType type) {
        Image img = new Image(getClass().getResourceAsStream("/OKAY.jpg"));
        ImageView icon = new ImageView(img);

        // Set a smaller image size
        icon.setFitWidth(30);  // Adjust width
        icon.setFitHeight(30); // Adjust height

        Notifications notification = Notifications.create()
                .title(title)
                .text(message)
                .graphic(icon)
                .hideAfter(Duration.seconds(4))
                .position(Pos.TOP_RIGHT)
                .onAction(e -> System.out.println("Notification clicked!"));

        // Apply custom icons based on notification type
        switch (type) {
            case SUCCESS:
                icon.setImage(new Image(getClass().getResourceAsStream("/OKAY.jpg")));
                break;
            case ERROR:
                icon.setImage(new Image(getClass().getResourceAsStream("/OKAY.jpg")));
                break;
            case WARNING:
                icon.setImage(new Image(getClass().getResourceAsStream("/OKAY.jpg")));
                break;
        }

        notification.show();
    }

    // Enum for notification types
    enum NotificationType {
        SUCCESS, ERROR, WARNING
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


    @FXML
    public void recherche(){
        if (filterField == null || filterField.getText().isEmpty()) {
            recTab.setItems(destinationList);  // Show all data if no filter
        } else {
            ObservableList<Objet> filteredData = FXCollections.observableArrayList();
            for (Objet item : destinationList) {
                if (item.getStatus().toLowerCase().contains(filterField.getText().toLowerCase()) ||
                        item.getNom().toLowerCase().contains(filterField.getText().toLowerCase())) {
                    filteredData.add(item);
                }
            }
            recTab.setItems(filteredData);
        }

    }

    @FXML
    private void CHAT(ActionEvent event) {
        try {
            // Load the Chatbot.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chatbot.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new Stage for the Chatbot interface
            Stage stage = new Stage();
            stage.setTitle("Chatbot");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            CloudinaryService cloudinaryService = new CloudinaryService();
            String imageUrl = cloudinaryService.uploadImage(selectedFile);

            if (imageUrl != null) {
                showAlert("Success", "Image uploaded successfully!\nURL: " + imageUrl);
                System.out.println("Uploaded Image URL: " + imageUrl);
            } else {
                showAlert("Error", "Image upload failed.");
            }
        }
    }
}