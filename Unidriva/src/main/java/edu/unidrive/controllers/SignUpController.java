package edu.unidrive.controllers;

import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SignUpController {

    @FXML
    private Button btnSave;

    @FXML
    private PasswordField password;

    @FXML
    private DatePicker txtDOB;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstname;

    @FXML
    private ComboBox<String> txtGender;

    @FXML
    private TextField txtLastname;

    @FXML
    void save(ActionEvent event) {
        // Vérifier les champs vides
        if (txtEmail.getText().isEmpty() || txtFirstname.getText().isEmpty() ||
                txtLastname.getText().isEmpty() || password.getText().isEmpty() ||
                txtGender.getSelectionModel().getSelectedItem() == null ||
                txtDOB.getValue() == null) {

            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // Récupérer les valeurs
        String email = txtEmail.getText();
        String firstname = txtFirstname.getText();
        String lastname = txtLastname.getText();
        String gender = txtGender.getSelectionModel().getSelectedItem();
        String pass = password.getText();
        String dob = txtDOB.getValue().toString();

        Utilisateur user = new Utilisateur(email, dob, firstname, lastname, gender, pass);
        UserService userService = new UserService();
        userService.add(user);

        // Afficher une alerte de succès
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès !");
        clearFields();
    }

    // Méthode pour afficher une alerte
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour vider les champs
    private void clearFields() {
        txtEmail.clear();
        txtFirstname.clear();
        txtLastname.clear();
        password.clear();
        txtGender.getSelectionModel().clearSelection();
        txtDOB.setValue(null);
    }
}
