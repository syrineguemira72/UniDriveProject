package edu.unidrive.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Trajet {
    private int id;
    private String pointDepart;
    private String pointArrive;
    private Timestamp heureDepart;
    private int dureeEstimee;
    private float distance;
    private int placeDisponible;
    private List<Reservation> reservations;



    public Trajet (int id, String pointDepart, String pointArrive, LocalDateTime heureDepart, int dureeEstimee, float distance, int placeDisponible) {
        this.id = id;
        this.pointDepart = pointDepart;
        this.pointArrive = pointArrive;
        this.heureDepart = Timestamp.valueOf(heureDepart);
        this.dureeEstimee = dureeEstimee;
        this.distance = distance;
        this.placeDisponible = placeDisponible;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        List<Reservation> validReservations = new  ArrayList<>();

        for (Reservation reservation : reservations) {
            if (this.placeDisponible > 0) {
                validReservations.add(reservation);
                this.placeDisponible--;
            } else {
                System.out.println("Réservation refusée, il n'y a pas de places disponibles !");
            }
        }

        this.reservations = validReservations;
    }

    public Trajet() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return heureDepart.toLocalDateTime();
    }

    public void setHeureDepart(LocalDateTime heureDepart) {
        this.heureDepart = Timestamp.valueOf(heureDepart);
    }

    public int getDureeEstimee() {
        return dureeEstimee;
    }

    public void setDureeEstimee(int dureeEstimee) {
        this.dureeEstimee = dureeEstimee;
    }

    public float getDistance() {
        return distance;
    }

    public int getPlaceDisponible() {
        return placeDisponible;
    }

    public void setPlaceDisponible(int placeDisponible) {
        this.placeDisponible = placeDisponible;
    }

    public void setDistance(float distance) {
        this.distance = distance;
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
                ", placesDisponibles=" + placeDisponible +
                '}';
    }
}
