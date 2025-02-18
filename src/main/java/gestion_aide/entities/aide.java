package gestion_aide.entities;

import javafx.beans.property.*;

public class aide {
    private IntegerProperty id;
    private StringProperty type;
    private StringProperty description;
    private StringProperty montant;

    // No-argument constructor
    public aide() {
        this.id = new SimpleIntegerProperty();
        this.type = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.montant = new SimpleStringProperty();
    }

    // Constructor with parameters for type, description, and montant
    public aide(String type, String description, String montant) {
        this.id = new SimpleIntegerProperty();
        this.type = new SimpleStringProperty(type);
        this.description = new SimpleStringProperty(description);
        this.montant = new SimpleStringProperty(montant);
    }

    // Getter and setter methods for all fields

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getMontant() {
        return montant.get();
    }

    public StringProperty montantProperty() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant.set(montant);
    }
}
