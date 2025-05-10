package edu.unidrive.entities;

import javafx.beans.property.*;

public class aide {
    private IntegerProperty id;
    private StringProperty currency;
    private StringProperty description;
    private StringProperty montant;
    private StringProperty createdAt; // New field
    private IntegerProperty idUser;        // FK to Utilisateur
    private IntegerProperty associationId; // FK to Association

    // No-arg constructor
    public aide() {
        this.id = new SimpleIntegerProperty();
        this.currency = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.montant = new SimpleStringProperty();
        this.createdAt = new SimpleStringProperty();
        this.idUser = new SimpleIntegerProperty();
        this.associationId = new SimpleIntegerProperty();
    }

    // Constructor without id (for insert)
    public aide(String currency, String description, String montant,
                String createdAt, int idUser, int associationId) {
        this.id = new SimpleIntegerProperty();
        this.currency = new SimpleStringProperty(currency);
        this.description = new SimpleStringProperty(description);
        this.montant = new SimpleStringProperty(montant);
        this.createdAt = new SimpleStringProperty(createdAt);
        this.idUser = new SimpleIntegerProperty(idUser);
        this.associationId = new SimpleIntegerProperty(associationId);
    }

    // Full constructor
    public aide(int id, String currency, String description, String montant,
                String createdAt, int idUser, int associationId) {
        this.id = new SimpleIntegerProperty(id);
        this.currency = new SimpleStringProperty(currency);
        this.description = new SimpleStringProperty(description);
        this.montant = new SimpleStringProperty(montant);
        this.createdAt = new SimpleStringProperty(createdAt);
        this.idUser = new SimpleIntegerProperty(idUser);
        this.associationId = new SimpleIntegerProperty(associationId);
    }

    public aide(String updatedCurrency, String updatedDescription, String updatedMontant) {

    }

    // Getters & Setters for id
    public int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public void setId(int id) {
        this.id.set(id);
    }

    // Currency
    public String getCurrency() {
        return currency.get();
    }
    public StringProperty currencyProperty() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    // Description
    public String getDescription() {
        return description.get();
    }
    public StringProperty descriptionProperty() {
        return description;
    }
    public void setDescription(String description) {
        this.description.set(description);
    }

    // Montant
    public String getMontant() {
        return montant.get();
    }
    public StringProperty montantProperty() {
        return montant;
    }
    public void setMontant(String montant) {
        this.montant.set(montant);
    }

    // CreatedAt
    public String getCreatedAt() {
        return createdAt.get();
    }
    public StringProperty createdAtProperty() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt.set(createdAt);
    }

    // idUser (foreign key)
    public int getIdUser() {
        return idUser.get();
    }
    public IntegerProperty idUserProperty() {
        return idUser;
    }
    public void setIdUser(int idUser) {
        this.idUser.set(idUser);
    }

    // associationId (foreign key)
    public int getAssociationId() {
        return associationId.get();
    }
    public IntegerProperty associationIdProperty() {
        return associationId;
    }
    public void setAssociationId(int associationId) {
        this.associationId.set(associationId);
    }

    @Override
    public String toString() {
        return "aide{" +
                "id=" + getId() +
                ", currency='" + getCurrency() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", montant='" + getMontant() + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", idUser=" + getIdUser() +
                ", associationId=" + getAssociationId() +
                '}';
    }
}