package edu.unidrive.services;

import edu.unidrive.entities.Trajet;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrajetService implements Iservice<Trajet> {


    @Override
    public void addEntity(Trajet trajet) {
        try {
            String req = " INSERT INTO trajet (id, pointDepart, pointArrive,heureDepart,dureeEstimee,distance,placeDisponible)"
                    + " VALUES (?,?,?,?,?,?,?)";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);


            pst.setInt(1, trajet.getId());
            pst.setString(2, trajet.getPointDepart());
            pst.setString(3, trajet.getPointArrive());
            pst.setTimestamp(4, Timestamp.valueOf(trajet.getHeureDepart()));
            pst.setInt(5, trajet.getDureeEstimee());
            pst.setFloat(6, trajet.getDistance());
            pst.setInt(7, trajet.getPlaceDisponible());
            pst.executeUpdate();
            System.out.println("trajet ajouté avec succès");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(int id, Trajet trajet) {

    }

    @Override
    public void updateEntity(int id, Trajet trajet) {
        try {
            String req = "UPDATE trajet SET " +
                    "pointDepart = ?, " +
                    "pointArrive = ?," +
                    " heureDepart = ?," +
                    " dureeEstimee = ?," +
                    " distance = ?," +
                    " placeDisponible = ?" +
                    " WHERE id = ?";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setString(1, trajet.getPointDepart());
            pst.setString(2, trajet.getPointArrive());
            pst.setTimestamp(3, Timestamp.valueOf(trajet.getHeureDepart()));
            pst.setInt(4, trajet.getDureeEstimee());
            pst.setFloat(5, trajet.getDistance());
            pst.setInt(6, trajet.getPlaceDisponible());
            pst.setInt(7, id);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Trajet mis à jour avec succès ");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du trajet : " + e.getMessage());
        }
    }

    @Override
    public List<Trajet> getallData() {
        return List.of();
    }

    @Override
    public void deleteEntity(Trajet trajet) {
        try {
            // Step 1: Check if there are associated reservations
            String checkReservationsQuery = "SELECT COUNT(*) FROM reservation WHERE trajet = ?";
            PreparedStatement checkReservationsStmt = MyConnection.getInstance().getCnx().prepareStatement(checkReservationsQuery);
            checkReservationsStmt.setInt(1, trajet.getId());
            ResultSet rs = checkReservationsStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // If there are reservations, throw an exception or return a message
                throw new SQLException("Ce trajet a des réservations associées. Suppression impossible.");
            }

            // Step 2: Delete the trip
            String deleteTripQuery = "DELETE FROM trajet WHERE id = ?";
            PreparedStatement deleteTripStmt = MyConnection.getInstance().getCnx().prepareStatement(deleteTripQuery);
            deleteTripStmt.setInt(1, trajet.getId());

            int rowsDeleted = deleteTripStmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Trajet supprimé avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du trajet : " + e.getMessage());
            throw new RuntimeException(e); // Propagate the exception to the controller
        }
    }

    @Override
    public void add(Trajet entity) {

    }

    @Override
    public void remove(Trajet entity) {

    }

    @Override
    public void update(Trajet entity) {

    }

    @Override
    public List<Trajet> getAllData() {
        List<Trajet> result = new ArrayList<>();
        try {
            String req = "SELECT * FROM trajet";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Trajet trajet = new Trajet();
                trajet.setId(rs.getInt("id"));
                trajet.setPointDepart(rs.getString("pointDepart"));
                trajet.setPointArrive(rs.getString("pointArrive"));
                trajet.setHeureDepart(rs.getTimestamp("heureDepart").toLocalDateTime());
                trajet.setDureeEstimee(rs.getInt("dureeEstimee"));
                trajet.setDistance(rs.getFloat("distance"));
                trajet.setPlaceDisponible(rs.getInt("placeDisponible"));
                result.add(trajet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des trajets : " + e.getMessage());
        }
        return result;
    }

    @Override
    public void removeEntity(Trajet entity) {

    }

    @Override
    public void updateEntity(Trajet entity) {

    }

    @Override
    public Trajet getEntity(int id) {
        Trajet trajet = new Trajet();
        try {
            String req = "SELECT * FROM trajet WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                trajet.setId(rs.getInt("id"));
                trajet.setPointDepart(rs.getString("pointDepart"));
                trajet.setPointArrive(rs.getString("pointArrive"));
                trajet.setHeureDepart(rs.getTimestamp("heureDepart").toLocalDateTime());
                trajet.setDureeEstimee(rs.getInt("dureeEstimee"));
                trajet.setDistance(rs.getFloat("distance"));
                trajet.setPlaceDisponible(rs.getInt("placeDisponible"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du trajet : " + e.getMessage());
        }
        return trajet;
    }

    public List<Trajet> getTrajetsByDate(LocalDate selectedDate) {
        List<Trajet> result = new ArrayList<>();
        try {
            // Convert the selectedDate to the start and end of the day
            LocalDateTime startOfDay = selectedDate.atStartOfDay();
            LocalDateTime endOfDay = selectedDate.atTime(23, 59, 59);

            String req = "SELECT * FROM trajet WHERE heureDepart BETWEEN ? AND ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setTimestamp(1, Timestamp.valueOf(startOfDay));
            pst.setTimestamp(2, Timestamp.valueOf(endOfDay));

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Trajet trajet = new Trajet();
                trajet.setId(rs.getInt("id"));
                trajet.setPointDepart(rs.getString("pointDepart"));
                trajet.setPointArrive(rs.getString("pointArrive"));
                trajet.setHeureDepart(rs.getTimestamp("heureDepart").toLocalDateTime());
                trajet.setDureeEstimee(rs.getInt("dureeEstimee"));
                trajet.setDistance(rs.getFloat("distance"));
                trajet.setPlaceDisponible(rs.getInt("placeDisponible"));
                result.add(trajet);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des trajets par date : " + e.getMessage());
        }
        return result;
    }
}
