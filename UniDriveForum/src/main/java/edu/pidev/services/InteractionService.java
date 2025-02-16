package edu.pidev.services;

import edu.pidev.entities.Interaction;
import edu.pidev.interfaces.Iservice;
import edu.pidev.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InteractionService implements Iservice<Interaction> {
    private final Connection cnx = MyConnection.getInstance().getCnx(); // Singleton

    @Override
    public void addEntity(Interaction entity) {
        try {
            String requete = "INSERT INTO `interaction`(`content`, `date`, `postId`) VALUES (?, ?, ?)";
            PreparedStatement pst = cnx.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, entity.getContent());
            pst.setDate(2, Date.valueOf(entity.getDate()));
            pst.setInt(3, entity.getPostId());

            pst.executeUpdate();

            // Récupérer l'ID généré
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(1));
            }

            System.out.println("Interaction ajoutée avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void removeEntity(Interaction entity) {
        String requete = "DELETE FROM `interaction` WHERE id = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, entity.getId());

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Interaction supprimée avec succès !");
            } else {
                System.out.println("Aucune interaction trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(Interaction entity) {
        String requete = "UPDATE `interaction` SET `content` = ?, `date` = ? WHERE `id` = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, entity.getContent());
            pst.setDate(2, Date.valueOf(entity.getDate()));
            pst.setInt(3, entity.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Interaction mise à jour avec succès !");
            } else {
                System.out.println("Aucune interaction trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public List<Interaction> getAllData() {
        List<Interaction> result = new ArrayList<>();
        String requete = "SELECT * FROM interaction";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Interaction t = new Interaction();
                t.setId(rs.getInt("id"));
                t.setContent(rs.getString("content"));
                t.setDate(rs.getDate("date").toLocalDate());
                t.setPostId(rs.getInt("postId"));
                result.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
}
