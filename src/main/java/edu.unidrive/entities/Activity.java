package edu.unidrive.entities;
import java.util.Date;

public class Activity {
    private int id;
    private String titre;
    private String description;
    private Date date;

    // Constructor
    public Activity(int id, String titre, String description, Date date) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
    }

    // Default Constructor (for empty objects)
    public Activity() {
        this(0, "", "", new Date());
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Override toString for debugging purposes
    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
