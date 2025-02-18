package ACT.entities;

import java.time.LocalDateTime;

public class Activite {
    private int id;
    private String nom;
    private String description;
    private LocalDateTime dateHeure;
    private String lieu;


    public Activite() {}

    public Activite(int id, String nom, String description, LocalDateTime dateHeure, String lieu) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateHeure = dateHeure;
        this.lieu = lieu;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
}
