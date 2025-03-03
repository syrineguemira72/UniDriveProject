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
import org.mindrot.jbcrypt.BCrypt;


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
                utilisateur.setRole(rs.getString("role")); // Ajoutez cette ligne

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
    public int getUserIdByEmail(String email) {


        Utilisateur user1 = getUserByEmail(email);
        if (user1 != null) {
            return user1.getId(); // Retourner l'ID de l'utilisateur
        } else {
            throw new RuntimeException("Utilisateur non trouvé avec l'email : " + email);
        }
    }

    public void add(Utilisateur utilisateur) {
        String requete = "INSERT INTO utilisateur (email, dob, gender, firstname, lastname, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, utilisateur.getEmail());
            pst.setString(2, utilisateur.getDob());
            pst.setString(3, utilisateur.getGender());
            pst.setString(4, utilisateur.getFirstname());
            pst.setString(5, utilisateur.getLastname());
            pst.setString(6, utilisateur.getPassword());
            pst.setString(7, utilisateur.getRole());

            pst.executeUpdate();

            // Récupérer l'ID généré par la base de données
            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    utilisateur.setId(generatedKeys.getInt(1));

                } else {
                    throw new SQLException("Échec de la récupération de l'ID généré.");
                }
            }
            System.out.println("Utilisateur ajouté avec succès !");
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
                        rs.getString("password"),
                        rs.getString("role")
                );
                utilisateurs.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(" Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    @Override
    public void removeEntity(Utilisateur entity) {

    }

    @Override
    public void updateEntity(Utilisateur entity) {

    }

    @Override
    public void addEntity(Utilisateur utilisateur) {

    }

    @Override
    public void deleteEntity(int id, Utilisateur utilisateur) {

    }

    @Override
    public void updateEntity(int id, Utilisateur utilisateur) {

    }

    @Override
    public List<Utilisateur> getallData() {
        return List.of();
    }

    @Override
    public Utilisateur getEntity(int id) {
        return null;
    }

    @Override
    public void deleteEntity(Utilisateur utilisateur) {

    }

    public boolean loginUser(String email, String password) {
        try {
            String requete = "SELECT password FROM utilisateur WHERE email = ?";
            PreparedStatement pst = connection.prepareStatement(requete);
            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(password, hashedPassword); // Vérifie le mot de passe haché
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
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de l'unicité de l'email : " + e.getMessage());
        }
        return false;
    }
    public String getHashedPasswordByEmail(String email) {
        String query = "SELECT password FROM utilisateur WHERE email = ?";

        try (
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public int getTotalUsers() {
        String query = "SELECT COUNT(*) FROM utilisateur";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAdminUsers() {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE role = 'ADMIN'";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



}
