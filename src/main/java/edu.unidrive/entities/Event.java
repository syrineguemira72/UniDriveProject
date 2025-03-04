package edu.unidrive.entities;
import java.util.Date;

public class Event {
    private int id;
    private String titre;
    private String description;
    private Date date;
    private String activities; // Store as a single string

    // Constructor
    public Event(int id, String titre, String description, Date date, String activities) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.activities = activities; // Store activities as a single string
    }

    // Default Constructor (for empty objects)
    public Event() {
        this(0, "", "", new Date(), "");
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

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    // Override toString for debugging purposes
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", activities='" + activities + '\'' +
                '}';
    }
}
