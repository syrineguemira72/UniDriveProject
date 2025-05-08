package edu.unidrive.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.unidrive.entities.Utilisateur;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UserService implements Iservice<Utilisateur> {

    private Connection connection;

    public UserService() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    public Utilisateur getUserByEmail(String email) {
        Utilisateur utilisateur = null;
        String requete = "SELECT * FROM utilisateur WHERE email = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(requete);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                utilisateur = mapResultSetToUtilisateur(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return utilisateur;
    }
    @Override
    public void add(Utilisateur utilisateur) {
        String requete = "INSERT INTO utilisateur (email, password, gender, firstname, lastname, dob, " +
                "street, governorate, phone_number, image_url, image_size, about_me, " +
                "is_active, created_at, reset_token, reset_token_expires_at, is_banned, ban_end_date, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)) {
            String hashedPassword = BCrypt.hashpw(utilisateur.getPassword().trim(), BCrypt.gensalt(12));

            String rolesJson = convertRolesToJson(utilisateur.getRoles());


            pst.setString(1, utilisateur.getEmail());
            pst.setString(2, hashedPassword);
            pst.setString(3, utilisateur.getGender());
            pst.setString(4, utilisateur.getFirstname());
            pst.setString(5, utilisateur.getLastname());
            pst.setDate(6, utilisateur.getDob() != null ? Date.valueOf(utilisateur.getDob()) : null);
            pst.setString(7, utilisateur.getStreet());
            pst.setString(8, utilisateur.getGovernorate());
            pst.setString(9, utilisateur.getPhoneNumber());
            pst.setString(10, utilisateur.getImageUrl());
            pst.setObject(11, utilisateur.getImageSize(), Types.INTEGER);
            pst.setString(12, utilisateur.getAboutMe());
            pst.setBoolean(13, utilisateur.isActive());
            pst.setTimestamp(14, Timestamp.valueOf(utilisateur.getCreatedAt()));
            pst.setString(15, utilisateur.getResetToken());
            pst.setTimestamp(16, utilisateur.getResetTokenExpiresAt() != null ?
                    Timestamp.valueOf(utilisateur.getResetTokenExpiresAt()) : null);
            pst.setBoolean(17, utilisateur.isBanned());
            pst.setTimestamp(18, utilisateur.getBanEndDate() != null ?
                    Timestamp.valueOf(utilisateur.getBanEndDate()) : null);
            pst.setString(19, rolesJson); // Stockage au format JSON

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    utilisateur.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage(), e);
        }

    }


    @Override
    public void update(Utilisateur utilisateur) {
        String requete = "UPDATE utilisateur SET " +
                "email=?, dob=?, gender=?, firstname=?, lastname=?, " +
                "street=?, governorate=?, phone_number=?, image_url=?, image_size=?, " +
                "about_me=?, is_active=?, updated_at=?, last_login_at=?, " +
                "reset_token=?, reset_token_expires_at=?, is_banned=?, ban_end_date=?, role=? " +
                "WHERE id=?";

        try (PreparedStatement pst = connection.prepareStatement(requete)) {
            // Prendre le premier rôle ou ROLE_USER par défaut
            String rolesJson = convertRolesToJson(utilisateur.getRoles());


            pst.setString(1, utilisateur.getEmail());
            pst.setDate(2, utilisateur.getDob() != null ? Date.valueOf(utilisateur.getDob()) : null);
            pst.setString(3, utilisateur.getGender());
            pst.setString(4, utilisateur.getFirstname());
            pst.setString(5, utilisateur.getLastname());
            pst.setString(6, utilisateur.getStreet());
            pst.setString(7, utilisateur.getGovernorate());
            pst.setString(8, utilisateur.getPhoneNumber());
            pst.setString(9, utilisateur.getImageUrl());
            pst.setObject(10, utilisateur.getImageSize(), Types.INTEGER);
            pst.setString(11, utilisateur.getAboutMe());
            pst.setBoolean(12, utilisateur.isActive());
            pst.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()));
            pst.setTimestamp(14, utilisateur.getLastLoginAt() != null ?
                    Timestamp.valueOf(utilisateur.getLastLoginAt()) : null);
            pst.setString(15, utilisateur.getResetToken());
            pst.setTimestamp(16, utilisateur.getResetTokenExpiresAt() != null ?
                    Timestamp.valueOf(utilisateur.getResetTokenExpiresAt()) : null);
            pst.setBoolean(17, utilisateur.isBanned());
            pst.setTimestamp(18, utilisateur.getBanEndDate() != null ?
                    Timestamp.valueOf(utilisateur.getBanEndDate()) : null);
            pst.setString(19, rolesJson); // Utilisation du champ role au singulier
            pst.setInt(20, utilisateur.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword.trim());
    }
    @Override
    public void remove(Utilisateur utilisateur) {
        try {
            // D'abord supprimer les rôles
            String deleteRolesQuery = "DELETE FROM utilisateur_roles WHERE utilisateur_id = ?";
            try (PreparedStatement deleteRolesStmt = connection.prepareStatement(deleteRolesQuery)) {
                deleteRolesStmt.setInt(1, utilisateur.getId());
                deleteRolesStmt.executeUpdate();
            }

            // Puis supprimer l'utilisateur
            String deleteUserQuery = "DELETE FROM utilisateur WHERE id = ?";
            try (PreparedStatement deleteUserStmt = connection.prepareStatement(deleteUserQuery)) {
                deleteUserStmt.setInt(1, utilisateur.getId());
                int rowsDeleted = deleteUserStmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Utilisateur supprimé avec succès !");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public List<Utilisateur> getAllData() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String requete = "SELECT u.*, GROUP_CONCAT(ur.role) as roles FROM utilisateur u " +
                "LEFT JOIN utilisateur_roles ur ON u.id = ur.utilisateur_id " +
                "GROUP BY u.id";
        try (PreparedStatement pst = connection.prepareStatement(requete);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getInt("id"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setDob(rs.getDate("dob") != null ? rs.getDate("dob").toLocalDate() : null);
        utilisateur.setGender(rs.getString("gender"));
        utilisateur.setFirstname(rs.getString("firstname"));
        utilisateur.setLastname(rs.getString("lastname"));
        utilisateur.setPassword(rs.getString("password"));
        utilisateur.setStreet(rs.getString("street"));
        utilisateur.setGovernorate(rs.getString("governorate"));
        utilisateur.setPhoneNumber(rs.getString("phone_number"));
        utilisateur.setImageUrl(rs.getString("image_url"));
        utilisateur.setImageSize(rs.getObject("image_size", Integer.class));
        utilisateur.setAboutMe(rs.getString("about_me"));
        utilisateur.setActive(rs.getBoolean("is_active"));
        utilisateur.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        utilisateur.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        utilisateur.setLastLoginAt(rs.getTimestamp("last_login_at") != null ?
                rs.getTimestamp("last_login_at").toLocalDateTime() : null);
        utilisateur.setResetToken(rs.getString("reset_token"));
        utilisateur.setResetTokenExpiresAt(rs.getTimestamp("reset_token_expires_at") != null ?
                rs.getTimestamp("reset_token_expires_at").toLocalDateTime() : null);
        utilisateur.setBanned(rs.getBoolean("is_banned"));
        utilisateur.setBanEndDate(rs.getTimestamp("ban_end_date") != null ?
                rs.getTimestamp("ban_end_date").toLocalDateTime() : null);

        // Gestion des rôles
        String rolesJson = rs.getString("role");
        List<String> roles = convertJsonToRoles(rolesJson);
        utilisateur.setRoles(roles);

        return utilisateur;
    }

    public boolean loginUser(String email, String password) {
        Utilisateur utilisateur = getUserByEmail(email);
        if (utilisateur != null) {
            return BCrypt.checkpw(password, utilisateur.getPassword());
        }
        return false;
    }

    public boolean isEmailUnique(String email) {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
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

    public void updatePassword(int userId, String newPassword) {
        String query = "UPDATE utilisateur SET password = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            pst.setInt(2, userId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
        }
    }

    public void banUser(int userId, LocalDateTime banEndDate) {
        String query = "UPDATE utilisateur SET is_banned = true, ban_end_date = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setTimestamp(1, banEndDate != null ? Timestamp.valueOf(banEndDate) : null);
            pst.setInt(2, userId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du bannissement de l'utilisateur : " + e.getMessage());
        }
    }

    public void unbanUser(int userId) {
        String query = "UPDATE utilisateur SET is_banned = false, ban_end_date = NULL WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, userId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du débannissement de l'utilisateur : " + e.getMessage());
        }
    }

    // Implémentations des méthodes non utilisées de l'interface
    @Override
    public void removeEntity(Utilisateur entity) {
        remove(entity);
    }

    @Override
    public void updateEntity(Utilisateur entity) {
        update(entity);
    }

    @Override
    public void addEntity(Utilisateur utilisateur) {
        add(utilisateur);
    }

    @Override
    public void deleteEntity(int id, Utilisateur utilisateur) {
        remove(utilisateur);
    }

    @Override
    public void updateEntity(int id, Utilisateur utilisateur) {
        update(utilisateur);
    }

    @Override
    public List<Utilisateur> getallData() {
        return getAllData();
    }

    @Override
    public Utilisateur getEntity(int id) {
        String query = "SELECT u.*, GROUP_CONCAT(ur.role) as roles FROM utilisateur u " +
                "LEFT JOIN utilisateur_roles ur ON u.id = ur.utilisateur_id " +
                "WHERE u.id = ? " +
                "GROUP BY u.id";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteEntity(Utilisateur utilisateur) {
        remove(utilisateur);
    }
    public void testBcryptIntegration() {
        String testPassword = "Test1234";
        String hashed = BCrypt.hashpw(testPassword, BCrypt.gensalt(12));

        System.out.println("=== BCrypt Integration Test ===");
        System.out.println("Test password: " + testPassword);
        System.out.println("Generated hash: " + hashed);
        System.out.println("Verification: " + verifyPassword(testPassword, hashed));

        // Test avec la base de données
        Utilisateur testUser = getUserByEmail("test@example.com");
        if (testUser != null) {
            System.out.println("Database hash: " + testUser.getPassword());
            System.out.println("Database verification: " +
                    verifyPassword(testPassword, testUser.getPassword()));
        }
    }
    // Add these methods to your UserService class

    public int getTotalUsers() {
        String query = "SELECT COUNT(*) FROM utilisateur";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total users: " + e.getMessage());
        }
        return 0;
    }

    public int getAdminUsers() {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE role LIKE '%\"ROLE_ADMIN\"%'";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting admin users: " + e.getMessage());
        }
        return 0;
    }
    private String convertRolesToJson(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return "[\"ROLE_USER\"]"; // Valeur par défaut
        }
        return new Gson().toJson(roles);}
    private List<String> convertJsonToRoles(String rolesJson) {
        if (rolesJson == null || rolesJson.isEmpty()) {
            return Collections.singletonList("ROLE_USER");
        }
        try {
            return new Gson().fromJson(rolesJson, new TypeToken<List<String>>(){}.getType());
        } catch (Exception e) {
            return Collections.singletonList("ROLE_USER");
        }}
}