package ACT.services;

import ACT.entities.Activite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Activite_Crud implements IService<Activite> {
    private Connection connection;

    // Constructor that accepts a Connection object
    public Activite_Crud(Connection connection) {
        this.connection = connection; // Assign the passed connection
    }

    @Override
    public void ajouter(Activite activite) throws SQLException {
        String query = "INSERT INTO activite (nom, description, dateHeure, lieu) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, activite.getNom());
            stmt.setString(2, activite.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(activite.getDateHeure()));
            stmt.setString(4, activite.getLieu());
            stmt.executeUpdate();
        }
    }

    @Override
    public void modifier(Activite activite) throws SQLException {
        String query = "UPDATE activite SET nom = ?, description = ?, dateHeure = ?, lieu = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, activite.getNom());
            stmt.setString(2, activite.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(activite.getDateHeure()));
            stmt.setString(4, activite.getLieu());
            stmt.setInt(5, activite.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void supprimer(Activite activite) throws SQLException {
        // Implement delete logic here
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM activite WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Activite> afficher() throws SQLException {
        List<Activite> activites = new ArrayList<>();
        String query = "SELECT * FROM activite";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Activite activite = new Activite(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getTimestamp("dateHeure").toLocalDateTime(),
                        rs.getString("lieu")
                );
                activites.add(activite);
            }
        }
        return activites;
    }
}
