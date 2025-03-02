package edu.unidrive.entities;

import javafx.beans.property.*;

public class aide {
    private IntegerProperty id;
    private StringProperty currency;
    private StringProperty description;
    private StringProperty montant;
    private StringProperty createdAt; // New field

    // Constructeur sans argument
    public aide() {
        this.id = new SimpleIntegerProperty();
        this.currency = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.montant = new SimpleStringProperty();
        this.createdAt = new SimpleStringProperty();
    }

    // Constructeur avec paramètres
    public aide(String currency, String description, String montant) {
        this.id = new SimpleIntegerProperty();
        this.currency = new SimpleStringProperty(currency);
        this.description = new SimpleStringProperty(description);
        this.montant = new SimpleStringProperty(montant);
        this.createdAt = new SimpleStringProperty();
    }

    // Getters et Setters

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getCurrency() {
        return currency.get();
    }

    public StringProperty currencyProperty() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
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

    public String getCreatedAt() {
        return createdAt.get();
    }

    public StringProperty createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt.set(createdAt);
    }
}
