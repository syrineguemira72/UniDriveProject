package edu.unidrive.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DetailController {

    @FXML
    private TextField currencytextfield;

    @FXML
    private TextField descriptiontextfield;

    @FXML
    private TextField montanttextfield;


    public TextField getCurrencytextfield() {
        return currencytextfield;
    }

    public void setCurrencytextfield(String currencytextfield) {
        this.currencytextfield.setText(currencytextfield);
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
