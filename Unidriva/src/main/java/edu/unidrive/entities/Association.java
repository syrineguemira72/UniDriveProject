package edu.unidrive.entities;

import javafx.beans.property.*;

public class Association {
    private IntegerProperty id;
    private StringProperty nom;
    private StringProperty adresse;
    private StringProperty telephone;
    private StringProperty email;
    private IntegerProperty aideId;

    // Default Constructor
    public Association() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.adresse = new SimpleStringProperty();
        this.telephone = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.aideId = new SimpleIntegerProperty();
    }

    // Constructor with parameters
    public Association(String nom, String adresse, String telephone, String email, int aideId) {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty(nom);
        this.adresse = new SimpleStringProperty(adresse);
        this.telephone = new SimpleStringProperty(telephone);
        this.email = new SimpleStringProperty(email);
        this.aideId = new SimpleIntegerProperty(aideId);
    }

    // Getters and Setters
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getAdresse() {
        return adresse.get();
    }

    public StringProperty adresseProperty() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }

    public String getTelephone() {
        return telephone.get();
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public Integer getAideId() {
        return aideId.get() == 0 ? null : aideId.get(); // Return null if it's 0 (or another default value)
    }

    public IntegerProperty aideIdProperty() {
        return aideId;
    }

    public void setAideId(Integer aideId) {
        this.aideId.set(aideId);
    }
}