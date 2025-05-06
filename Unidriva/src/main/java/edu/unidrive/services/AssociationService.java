package edu.unidrive.services;

import edu.unidrive.entities.Association;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssociationService implements Iservice<Association> {

    private Connection cnx;

    public AssociationService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(Association entity) {

    }

    @Override
    public void remove(Association entity) {

    }

    @Override
    public void update(Association entity) {

    }

    @Override
    public List<Association> getAllData() {
        return List.of();
    }

    @Override
    public void removeEntity(Association entity) {

    }

    @Override
    public void updateEntity(Association entity) {

    }

    @Override
    public void addEntity(Association association) {
        String requete = "INSERT INTO association (nom, adresse, telephone, email, aide_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, association.getNom());
            pst.setString(2, association.getAdresse());
            pst.setString(3, association.getTelephone());
            pst.setString(4, association.getEmail());
            pst.setInt(5, association.getAideId());  // Set the aide_id

            pst.executeUpdate();
            System.out.println("Association ajouté avec succès!");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
        }
    }


    @Override
    public void deleteEntity(int id, Association association) {
        String requete = "DELETE FROM association WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Association supprimé avec succès!");
            } else {
                System.out.println("Aucune association trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(int id, Association association) {
        String requete = "UPDATE association SET nom = ?, adresse = ?, telephone = ?, email = ?, aide_id = ? WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, association.getNom());
            pst.setString(2, association.getAdresse());
            pst.setString(3, association.getTelephone());
            pst.setString(4, association.getEmail());

            // Correctly handle aide_id (can be NULL)
            if (association.getAideId() == null) {
                pst.setNull(5, java.sql.Types.INTEGER);
            } else {
                pst.setInt(5, association.getAideId());
            }

            pst.setInt(6, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Association mis à jour avec succès!");
            } else {
                System.out.println("Aucun Assocation trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }




    @Override
    public List<Association> getallData() {
        List<Association> result = new ArrayList<>();
        String requete = "SELECT * FROM association";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(requete)) {

            while (rs.next()) {
                Association b = new Association();
                b.setId(rs.getInt("id"));
                b.setNom(rs.getString("nom"));
                b.setAdresse(rs.getString("adresse"));
                b.setTelephone(rs.getString("telephone"));
                b.setEmail(rs.getString("email"));
                b.setAideId(rs.getInt("aide_id"));  // Retrieve the aide_id
                result.add(b);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des données: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Association getEntity(int id) {
        return null;
    }

    @Override
    public void deleteEntity(Association association) {

    }

}
