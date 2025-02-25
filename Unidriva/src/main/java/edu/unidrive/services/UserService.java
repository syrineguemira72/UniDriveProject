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
import edu.unidrive.entities.Profile;


public class UserService implements Iservice<Utilisateur> {

    private Connection connection;

    public UserService() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    public Utilisateur getUserByEmail(String email) {
        Utilisateur utilisateur = null;
        String requete = "SELECT u.*, p.id as profile_id, p.photo, p.bio, p.telephone, p.adresse " +
                "FROM utilisateur u " +
                "LEFT JOIN profile p ON u.id = p.utilisateur_id " +
                "WHERE u.email = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(requete);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setDob(rs.getString("dob"));
                utilisateur.setGender(rs.getString("gender"));
                utilisateur.setLastname(rs.getString("lastname"));
                utilisateur.setFirstname(rs.getString("firstname"));
                utilisateur.setPassword(rs.getString("password"));

                Profile profile = new Profile();
                profile.setId(rs.getInt("profile_id"));
                profile.setPhoto(rs.getString("photo"));
                profile.setBio(rs.getString("bio"));
                profile.setTelephone(rs.getString("telephone"));
                profile.setAdresse(rs.getString("adresse"));

                utilisateur.setProfile(profile);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return utilisateur;
    }

    public void add(Utilisateur user) {
        try {
            String requete = "INSERT INTO utilisateur (email, dob, gender, lastname, firstname, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(requete, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getEmail());
            pst.setString(2, user.getDob());
            pst.setString(3, user.getGender());
            pst.setString(4, user.getLastname());
            pst.setString(5, user.getFirstname());
            pst.setString(6, user.getPassword());

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                user.setId(userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
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
                System.out.println(" Utilisateur supprimé avec succès !");
            } else {
                System.out.println(" Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(" Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
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
            pst.setInt(7, entity.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur mis à jour avec succès !");
            } else {
                System.out.println(" Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(" Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
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
            throw new RuntimeException(" Erreur lors de la récupération des utilisateurs : " + e.getMessage());
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
            if (rs.next()) {
                System.out.println("Connexion réussie !");
                return true;
            } else {
                System.out.println("Nom d'utilisateur ou mot de passe incorrect !");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la connexion : " + e.getMessage());
        }
    }

    public boolean isEmailUnique(String email) {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // If the count is 0, the email is unique
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de l'unicité de l'email : " + e.getMessage());
        }
        return false;
    }
}
