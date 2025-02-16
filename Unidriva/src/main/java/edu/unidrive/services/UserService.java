package edu.unidrive.services;

import edu.unidrive.entities.Utilisateur;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements Iservice<Utilisateur> {

    private Connection connection;

    public UserService() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(Utilisateur entity) {
        try {
            String requete = "INSERT INTO utilisateur (id, email, dob, gender, lastname, firstname, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(requete);
            pst.setInt(1, entity.getId());
            pst.setString(2, entity.getEmail());
            pst.setString(3, entity.getDob());
            pst.setString(4, entity.getGender());
            pst.setString(5, entity.getLastname());
            pst.setString(6, entity.getFirstname());
            pst.setString(7, entity.getPassword());

            pst.executeUpdate();
            System.out.println("✅ Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void remove(Utilisateur entity) {
        try {
            String requete = "DELETE FROM utilisateur WHERE id = ?";
            PreparedStatement pst = connection.prepareStatement(requete);
            pst.setInt(1, entity.getId());

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Utilisateur supprimé avec succès !");
            } else {
                System.out.println("❌ Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void update(Utilisateur entity) {
        try {
            String requete = "UPDATE utilisateur SET email=?, dob=?, gender=?, lastname=?, firstname=?, password=? WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(requete);
            pst.setString(1, entity.getEmail());
            pst.setString(2, entity.getDob());
            pst.setString(3, entity.getGender());
            pst.setString(4, entity.getLastname());
            pst.setString(5, entity.getFirstname());
            pst.setString(6, entity.getPassword());
            pst.setInt(7, entity.getId()); // Correction de l'index

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("❌ Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public List<Utilisateur> getAllData() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try {
            String requete = "SELECT * FROM utilisateur";
            PreparedStatement pst = connection.prepareStatement(requete);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Utilisateur user = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("dob"),
                        rs.getString("gender"),
                        rs.getString("lastname"),
                        rs.getString("firstname"),
                        rs.getString("password")
                );
                utilisateurs.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    public boolean loginUser(String email, String password) {
        try {
            String requete = "SELECT * FROM utilisateur WHERE email = ? AND password = ?";
            PreparedStatement pst = connection.prepareStatement(requete);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) { // Un utilisateur a été trouvé
                System.out.println("✅ Connexion réussie !");
                return true;
            } else {
                System.out.println("❌ Nom d'utilisateur ou mot de passe incorrect !");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la connexion : " + e.getMessage());
        }
    }
}
