package edu.unidrive.entities;

import javafx.beans.property.*;

public class Association {
    private IntegerProperty id;
    private StringProperty nom;
    private StringProperty adresse;
    private StringProperty telephone;
    private StringProperty email;
    private StringProperty description;
    private StringProperty image;

    // Default constructor
    public Association() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.adresse = new SimpleStringProperty();
        this.telephone = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.image = new SimpleStringProperty();
    }

    // Constructor with all fields except id
    public Association(String nom, String adresse, String telephone, String email, String description, String image) {
        this();
        this.nom.set(nom);
        this.adresse.set(adresse);
        this.telephone.set(telephone);
        this.email.set(email);
        this.description.set(description);
        this.image.set(image);
    }

    // ID
    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }
    public IntegerProperty idProperty() {
        return id;
    }

    // Nom
    public String getNom() {
        return nom.get();
    }
    public void setNom(String nom) {
        this.nom.set(nom);
    }
    public StringProperty nomProperty() {
        return nom;
    }

    // Adresse
    public String getAdresse() {
        return adresse.get();
    }
    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }
    public StringProperty adresseProperty() {
        return adresse;
    }

    // Telephone
    public String getTelephone() {
        return telephone.get();
    }
    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }
    public StringProperty telephoneProperty() {
        return telephone;
    }

    // Email
    public String getEmail() {
        return email.get();
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public StringProperty emailProperty() {
        return email;
    }

    // Description
    public String getDescription() {
        return description.get();
    }
    public void setDescription(String description) {
        this.description.set(description);
    }
    public StringProperty descriptionProperty() {
        return description;
    }

    // Image
    public String getImage() {
        return image.get();
    }
    public void setImage(String image) {
        this.image.set(image);
    }
    public StringProperty imageProperty() {
        return image;
    }

}
