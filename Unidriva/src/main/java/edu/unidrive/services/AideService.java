package edu.unidrive.services;

import edu.unidrive.entities.aide;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AideService implements Iservice<aide> {

    private final Connection cnx;

    public AideService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(aide entity) {

    }

    @Override
    public void remove(aide entity) {

    }

    @Override
    public void update(aide entity) {

    }

    @Override
    public List<aide> getAllData() {
        return List.of();
    }

    @Override
    public void removeEntity(aide entity) {

    }

    @Override
    public void updateEntity(aide entity) {

    }

    @Override
    public void addEntity(aide aide) {
        String requete = "INSERT INTO aide (id, type, description, montant) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, aide.getId());
            pst.setString(2, aide.getType());
            pst.setString(3, aide.getDescription());
            pst.setString(4, aide.getMontant());
            pst.executeUpdate();
            System.out.println("Aide ajoutée avec succès!");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @Override
    public void deleteEntity(int id, aide aide) {
        String requete = "DELETE FROM aide WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aide supprimée avec succès!");
            } else {
                System.out.println("Aucune aide trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(int id, aide aide) {
        String requete = "UPDATE aide SET type = ?, description = ?, montant = ? WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, aide.getType());
            pst.setString(2, aide.getDescription());
            pst.setString(3, aide.getMontant());
            pst.setInt(4, id);  // Set the ID to match the row in the database
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aide mise à jour avec succès!");
            } else {
                System.out.println("Aucune aide trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }


    @Override
    public List<aide> getallData() {
        List<aide> result = new ArrayList<>();
        String requete = "SELECT * FROM aide";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(requete)) {

            while (rs.next()) {
                aide p = new aide();
                p.setId(rs.getInt("id"));
                p.setType(rs.getString("type"));
                p.setDescription(rs.getString("description"));
                p.setMontant(rs.getString("montant"));
                result.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des données: " + e.getMessage());
        }
        return result;
    }
}
