package edu.unidrive.entities;

public class Objet {
    public int id;
    public String nom;
    public String status;
    public String date;
    public String description;
    public String lieu;
    public String categorie;
    public String imagePath;

    public Objet(int id, String nom, String status, String date, String description, String lieu, String categorie, String imagePath) {
        this.id = id;
        this.nom = nom;
        this.status = status;
        this.date = date;
        this.description = description;
        this.lieu = lieu;
        this.categorie = categorie;
        this.imagePath = imagePath;
    }

    public Objet(String nom, String status, String date, String description, String lieu, String categorie, String imagePath) {
        this.nom = nom;
        this.status = status;
        this.date = date;
        this.description = description;
        this.lieu = lieu;
        this.categorie = categorie;
        this.imagePath = imagePath;
    }

    public Objet() {}

    public Objet(String id) {
        this.id = Integer.parseInt(id);
    }

    public Objet(String description, int i, String type, String finder, String owner) {
    }

    public int getId() {
        return this.id;
    }

    public String getNom() {
        return nom;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getLieu() {
        return lieu;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Objet{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", lieu='" + lieu + '\'' +
                ", categorie='" + categorie + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Objet objet = (Objet) o;
        return id == objet.id &&
                nom.equals(objet.nom) &&
                date.equals(objet.date);
    }
}