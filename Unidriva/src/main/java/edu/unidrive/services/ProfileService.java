package edu.unidrive.services;

import edu.unidrive.entities.Profile;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProfileService implements Iservice<Profile> {
    private Connection connection;

    public ProfileService() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(Profile entity) {
        try {
            String requete = "INSERT INTO profile (photo, bio, telephone, adresse, utilisateur_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

            pst.setString(1, entity.getPhoto());
            pst.setString(2, entity.getBio());
            pst.setString(3, entity.getTelephone());
            pst.setString(4, entity.getAdresse());

            if (entity.getUtilisateur() == null) {
                throw new RuntimeException("L'utilisateur associé au profil est null.");
            }

            pst.setInt(5, entity.getUtilisateur().getId());

            pst.executeUpdate();
            System.out.println("Profil ajouté avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du profil : " + e.getMessage());
        }
    }

    @Override
    public void remove(Profile entity) {
        try {
            String requete = "DELETE FROM profile WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, entity.getId());
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("profile supprimé avec succès !");
            } else {
                System.out.println("Aucun profile trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de profile : " + e.getMessage());
        }
    }

    @Override
    public void update(Profile entity) {
        try {
            String requete = "UPDATE profile SET  photo=?, bio=?, telephone=?, adresse=? WHERE id=?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, entity.getPhoto());
            pst.setString(2, entity.getBio());
            pst.setString(3, entity.getTelephone());
            pst.setString(4, entity.getAdresse());
            pst.setInt(5, entity.getId());
            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("profile mis à jour avec succès !");
            } else {
                System.out.println("Aucun profile trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de profile : " + e.getMessage());
        }
    }

    @Override
    public List<Profile> getAllData() {
        return List.of();
    }

}