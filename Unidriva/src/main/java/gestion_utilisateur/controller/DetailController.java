package gestion_utilisateur.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DetailController {

    @FXML
    private TextField typetextfield;

    @FXML
    private TextField descriptiontextfield;

    @FXML
    private TextField montanttextfield;


    public TextField getTypetextfield() {
        return typetextfield;
    }

    public void setTypetextfield(String typetextfield) {
        this.typetextfield.setText(typetextfield);
    }

    public TextField getDescriptiontextfield() {
        return descriptiontextfield;
    }

    public void setDescriptiontextfield(String descriptiontextfield) {
        this.descriptiontextfield.setText(descriptiontextfield);
    }

    public TextField getMontanttextfield() {
        return montanttextfield;
    }

    public void setMontanttextfield(String montanttextfield) {
        this.montanttextfield.setText(montanttextfield);
    }
}
