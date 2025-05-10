package edu.unidrive.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Trajet {
    private Integer id;
    private String pointDepart;
    private String pointArrive;
    private Timestamp heureDepart;
    private Integer dureeEstimee;
    private Float distance;
    private Integer placeDisponible;
    private String preferences;
    private Float prix;
    private List<Reservation> reservations = new ArrayList<>();
    private Utilisateur user;

    public Trajet() {
    }

    public Trajet(Integer id, String pointDepart, String pointArrive, LocalDateTime heureDepart,
                  Integer dureeEstimee, Float distance, Integer placeDisponible,
                  String preferences, Float prix) {
        this.id = id;
        this.pointDepart = pointDepart;
        this.pointArrive = pointArrive;
        this.heureDepart = Timestamp.valueOf(heureDepart);
        this.dureeEstimee = dureeEstimee;
        this.distance = distance;
        this.placeDisponible = placeDisponible;
        this.preferences = preferences;
        this.prix = prix;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPointDepart() {
        return pointDepart;
    }

    public void setPointDepart(String pointDepart) {
        this.pointDepart = pointDepart;
    }

    public String getPointArrive() {
        return pointArrive;
    }

    public void setPointArrive(String pointArrive) {
        this.pointArrive = pointArrive;
    }

    public LocalDateTime getHeureDepart() {
        return heureDepart != null ? heureDepart.toLocalDateTime() : null;
    }

    public void setHeureDepart(LocalDateTime heureDepart) {
        this.heureDepart = Timestamp.valueOf(heureDepart);
    }

    public Integer getDureeEstimee() {
        return dureeEstimee;
    }

    public void setDureeEstimee(Integer dureeEstimee) {
        this.dureeEstimee = dureeEstimee;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Integer getPlaceDisponible() {
        return placeDisponible;
    }

    public void setPlaceDisponible(Integer placeDisponible) {
        this.placeDisponible = placeDisponible;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public Float getPrix() {
        return prix;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setTrajet(this);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setTrajet(null);
    }

    public LocalDateTime getHeureArrive() {
        if (heureDepart == null || dureeEstimee == null) {
            return null;
        }
        return heureDepart.toLocalDateTime().plusMinutes(dureeEstimee);
    }

    public String getFormattedTravelTime() {
        LocalDateTime arrival = getHeureArrive();
        if (heureDepart == null || arrival == null) {
            return "Heure non définie";
        }
        return String.format("%s - %s (%d min)",
                heureDepart.toLocalDateTime().toLocalTime(),
                arrival.toLocalTime(),
                dureeEstimee);
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        // Remove from previous user's trajets
        if (this.user != null) {
            this.user.getTrajets().remove(this);
        }

        // Set new user
        this.user = user;

        // Add to new user's trajets
        if (user != null && !user.getTrajets().contains(this)) {
            user.getTrajets().add(this);
        }
    }

    @Override
    public String toString() {
        return "Trajet{" +
                "id=" + id +
                ", pointDepart='" + pointDepart + '\'' +
                ", pointArrive='" + pointArrive + '\'' +
                ", heureDepart=" + heureDepart +
                ", dureeEstimee=" + dureeEstimee +
                ", distance=" + distance +
                ", placeDisponible=" + placeDisponible +
                ", preferences='" + preferences + '\'' +
                ", prix=" + prix +
                '}';
    }
}