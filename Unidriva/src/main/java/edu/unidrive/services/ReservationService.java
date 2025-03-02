package edu.unidrive.services;

import edu.unidrive.entities.Reservation;
import edu.unidrive.entities.Trajet;
import edu.unidrive.entities.Etat;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements Iservice<Reservation> {

    public void addEntity(Reservation reservation) {
        try {
            String req = "INSERT INTO reservation (id, trajet, etat, dateReservation) VALUES (?, ?, ?, ?)";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setInt(1, reservation.getId());
            pst.setInt(2, reservation.getTrajet().getId());
            pst.setString(3, reservation.getEtat().name());
            pst.setDate(4, reservation.getDateReservation());

            pst.executeUpdate();
            System.out.println("Réservation ajoutée avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réservation :" + e.getMessage());
        }
    }

    @Override
    public void deleteEntity(int id, Reservation reservation) {

    }

    @Override
    public void updateEntity(int id, Reservation reservation) {
        try {
            String req = "UPDATE reservation SET trajet = ?, etat = ?, dateReservation = ? WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);

            pst.setInt(1, reservation.getTrajet().getId());
            pst.setString(2, reservation.getEtat().name());
            pst.setDate(3, Date.valueOf(reservation.getDateReservation().toLocalDate()));
            pst.setInt(4, id);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Réservation mise à jour avec succès.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la réservation : " + e.getMessage());
        }
    }

    @Override
    public List<Reservation> getallData() {
        return List.of();
    }

    @Override
    public void deleteEntity(Reservation reservation) {
        try {
            String req = "DELETE FROM reservation WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setInt(1, reservation.getId());

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Réservation supprimée avec succès.");
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }

    @Override
    public void add(Reservation entity) {

    }

    @Override
    public void remove(Reservation entity) {

    }

    @Override
    public void update(Reservation entity) {

    }

    @Override
    public List<Reservation> getAllData() {
        List<Reservation> reservations = new ArrayList<>();
        try {
                String req = "SELECT r.id AS reservation_id, r.trajet, r.etat, r.dateReservation, " +
                    "t.id AS trajet_id, t.pointDepart, t.pointArrive, t.heureDepart, t.dureeEstimee, t.distance, t.placeDisponible " +
                    "FROM reservation r " +
                    "LEFT JOIN trajet t ON r.trajet = t.id";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("reservation_id"));
                reservation.setEtat(Etat.valueOf(rs.getString("etat")));
                reservation.setDateReservation(rs.getDate("dateReservation").toLocalDate());

                // Création de l'objet Trajet associé
                Trajet trajet = new Trajet();
                trajet.setId(rs.getInt("trajet_id"));
                trajet.setPointDepart(rs.getString("pointDepart"));
                trajet.setPointArrive(rs.getString("pointArrive"));
                trajet.setHeureDepart(rs.getTimestamp("heureDepart").toLocalDateTime());
                trajet.setDureeEstimee(rs.getInt("dureeEstimee"));
                trajet.setDistance(rs.getFloat("distance"));
                trajet.setPlaceDisponible(rs.getInt("placeDisponible"));

                reservation.setTrajet(trajet);
                reservations.add(reservation);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des réservations : " + e.getMessage());
        }
        return reservations;
    }

    @Override
    public void removeEntity(Reservation entity) {

    }

    @Override
    public void updateEntity(Reservation entity) {

    }

    @Override
    public Reservation getEntity(int id) {
        Reservation reservation = null;
        try {
            String req = "SELECT r.id AS reservation_id, r.trajet, r.etat, r.dateReservation, " +
                    "t.id AS trajet_id, t.pointDepart, t.pointArrive, t.heureDepart, t.dureeEstimee, t.distance, t.placeDisponible " +
                    "FROM reservation r " +
                    "INNER JOIN trajet t ON r.trajet = t.id " +
                    "WHERE r.id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                reservation = new Reservation();
                reservation.setId(rs.getInt("reservation_id"));
                reservation.setEtat(Etat.valueOf(rs.getString("etat")));
                reservation.setDateReservation(rs.getDate("dateReservation").toLocalDate());

                // Création de l'objet Trajet associé
                Trajet trajet = new Trajet();
                trajet.setId(rs.getInt("trajet_id"));
                trajet.setPointDepart(rs.getString("pointDepart"));
                trajet.setPointArrive(rs.getString("pointArrive"));
                trajet.setHeureDepart(rs.getTimestamp("heureDepart").toLocalDateTime());
                trajet.setDureeEstimee(rs.getInt("dureeEstimee"));
                trajet.setDistance(rs.getFloat("distance"));
                trajet.setPlaceDisponible(rs.getInt("placeDisponible"));

                reservation.setTrajet(trajet);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la réservation : " + e.getMessage());
        }
        return reservation;
    }

    public void updateEtatReservation(int id, Etat etat) {
        try {
            String req = "UPDATE reservation SET etat = ? WHERE id = ?";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setString(1, etat.name());
            pst.setInt(2, id);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("État de la réservation mis à jour avec succès");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'état de la réservation : " + e.getMessage());
        }
    }
}
