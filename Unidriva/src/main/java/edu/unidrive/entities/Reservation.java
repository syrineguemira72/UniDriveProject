package cov.pidev3A8.entities;


import javax.print.attribute.standard.PresentationDirection;
import java.sql.Date;
import java.time.LocalDate;

public class Reservation {
    private int id;
    private Trajet trajet;
    private Etat etat;
    private Date dateReservation;



    public Reservation(int id, Trajet trajet, Etat etat, LocalDate dateReservation) {
        this.id = id;
        this.trajet = trajet;
        this.etat = etat;
        this.dateReservation = Date.valueOf(dateReservation);
    }
    public Reservation(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat status) {
        this.etat = status;
    }

    public Date  getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = Date.valueOf(dateReservation);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", trajet=" + trajet +
                ", status='" + etat + '\'' +
                ", dateReservation=" + dateReservation +
                '}';
    }
}