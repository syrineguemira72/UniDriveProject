package edu.unidrive.entities;

import java.time.LocalDate;

public class Reservation {
    private Integer id;
    private LocalDate dateReservation;
    private Trajet trajet;
    private Etat status;
    private Utilisateur user;

    public Reservation() {
    }

    public Reservation(Integer id, LocalDate dateReservation, Trajet trajet, Etat status) {
        this.id = id;
        this.dateReservation = dateReservation;
        this.trajet = trajet;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public Etat getEtat() {
        return status;
    }

    public void setEtat(Etat status) {
        this.status = status;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        // Remove from previous user's reservations
        if (this.user != null) {
            this.user.getReservations().remove(this);
        }

        // Set new user
        this.user = user;

        // Add to new user's reservations
        if (user != null && !user.getReservations().contains(this)) {
            user.getReservations().add(this);
        }
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", dateReservation=" + dateReservation +
                ", trajet=" + trajet +
                ", status=" + status +
                '}';
    }
}