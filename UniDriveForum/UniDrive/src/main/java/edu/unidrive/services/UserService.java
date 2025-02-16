package edu.unidrive.services;

import edu.unidrive.entities.Utilisateur;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserService implements Iservice<Utilisateur> {
    @Override
    public void add(Utilisateur entity) {
        try {
            String requete = "INSERT INTO `utilisateur`(`idUtilisateur`, `nom`, `prenom`, `email`, `password`, `role`) VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, entity.getIdUtilisateur());
            pst.setString(2, entity.getNom());
            pst.setString(3, entity.getPrenom());
            pst.setString(4, entity.getEmail());
            pst.setString(5, entity.getPassword());
            pst.setString(6, entity.getRole());
            pst.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void remove(Utilisateur entity) {
        try {
            String requete = "DELETE FROM `utilisateur` WHERE `idUtilisateur` = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, entity.getIdUtilisateur());
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Utilisateur supprimé avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void update(Utilisateur entity) {
        try {
            String requete = "UPDATE `utilisateur` SET `nom`=?, `prenom`=?, `email`=?, `password`=?, `role`=? WHERE `idUtilisateur`=?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, entity.getNom());
            pst.setString(2, entity.getPrenom());
            pst.setString(3, entity.getEmail());
            pst.setString(4, entity.getPassword());
            pst.setString(5, entity.getRole());
            pst.setInt(6, entity.getIdUtilisateur());
            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public List<Utilisateur> getAllData() {
        return List.of();
    }
}
