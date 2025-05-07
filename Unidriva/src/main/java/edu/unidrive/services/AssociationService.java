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
    public void addEntity(Association assoc) {
        String sql = "INSERT INTO association (nom, adresse, telephone, email, description, image) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, assoc.getNom());
            pst.setString(2, assoc.getAdresse());
            pst.setString(3, assoc.getTelephone());
            pst.setString(4, assoc.getEmail());
            pst.setString(5, assoc.getDescription());
            pst.setString(6, assoc.getImage());

            pst.executeUpdate();
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    assoc.setId(keys.getInt(1));
                }
            }
            System.out.println("Association ajoutée avec succès (ID=" + assoc.getId() + ")");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'association: " + e.getMessage());
        }
    }

    @Override
    public void deleteEntity(int id, Association assoc) {
        String sql = "DELETE FROM association WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            int affected = pst.executeUpdate();
            if (affected > 0) {
                System.out.println("Association supprimée (ID=" + id + ")");
            } else {
                System.out.println("Aucune association trouvée pour l'ID=" + id);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'association: " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(int id, Association assoc) {
        String sql = "UPDATE association SET nom = ?, adresse = ?, telephone = ?, email = ?, description = ?, image = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, assoc.getNom());
            pst.setString(2, assoc.getAdresse());
            pst.setString(3, assoc.getTelephone());
            pst.setString(4, assoc.getEmail());
            pst.setString(5, assoc.getDescription());
            pst.setString(6, assoc.getImage());
            pst.setInt(7, id);

            int affected = pst.executeUpdate();
            if (affected > 0) {
                System.out.println("Association mise à jour (ID=" + id + ")");
            } else {
                System.out.println("Aucune association trouvée pour l'ID=" + id);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'association: " + e.getMessage());
        }
    }

    @Override
    public List<Association> getAllData() {
        List<Association> list = new ArrayList<>();
        String sql = "SELECT * FROM association";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Association assoc = new Association(
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("description"),
                        rs.getString("image")
                );
                assoc.setId(rs.getInt("id"));
                list.add(assoc);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des associations: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Association> getallData() {
        return getAllData();
    }

    @Override
    public Association getEntity(int id) {
        String sql = "SELECT * FROM association WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Association assoc = new Association(
                            rs.getString("nom"),
                            rs.getString("adresse"),
                            rs.getString("telephone"),
                            rs.getString("email"),
                            rs.getString("description"),
                            rs.getString("image")
                    );
                    assoc.setId(rs.getInt("id"));
                    return assoc;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'association: " + e.getMessage());
        }
        return null;
    }

    // Legacy interface methods delegate to modern implementations
    @Override public void add(Association entity) { addEntity(entity); }
    @Override public void remove(Association entity) { deleteEntity(entity.getId(), entity); }
    @Override public void update(Association entity) { updateEntity(entity.getId(), entity); }
    @Override public void removeEntity(Association entity) { deleteEntity(entity.getId(), entity); }
    @Override public void updateEntity(Association entity) { updateEntity(entity.getId(), entity); }
    @Override public void deleteEntity(Association entity) { deleteEntity(entity.getId(), entity); }
}
