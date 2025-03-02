package edu.unidrive.services;

import edu.unidrive.entities.Interaction;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InteractionService implements Iservice<Interaction> {
    private final Connection cnx = MyConnection.getInstance().getCnx(); // Singleton
    private final TextFilterService textFilterService = new TextFilterService(); // Ajoutez cette ligne

    @Override
    public void addEntity(Interaction entity) {
        try {
            String requete = "INSERT INTO `interaction`(`content`, `date`, `postId`) VALUES (?, ?, ?)";
            PreparedStatement pst = cnx.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, entity.getContent());
            pst.setDate(2, Date.valueOf(entity.getDate()));
            pst.setInt(3, entity.getPostId());

            pst.executeUpdate();

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
    public void deleteEntity(int id, Interaction interaction) {

    }

    @Override
    public void updateEntity(int id, Interaction interaction) {

    }

    @Override
    public List<Interaction> getallData() {
        return List.of();
    }

    @Override
    public Interaction getEntity(int id) {
        return null;
    }

    @Override
    public void deleteEntity(Interaction interaction) {

    }

    @Override
    public void removeEntity(Interaction entity) {
        String requete = "DELETE FROM `interaction` WHERE id = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, entity.getId());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
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

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post mis à jour avec succès !");
            } else {
                System.out.println("Aucun post trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du post : " + e.getMessage());
        }
    }

    @Override
    public void add(Interaction entity) {

    }

    @Override
    public void remove(Interaction entity) {

    }

    @Override
    public void update(Interaction entity) {

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
    public Interaction getCommentByContent(String content) {
        String query = "SELECT * FROM interaction WHERE content = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, content);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Interaction comment = new Interaction();
                comment.setId(rs.getInt("id"));
                comment.setContent(rs.getString("content"));
                comment.setDate(rs.getDate("date").toLocalDate());
                comment.setPostId(rs.getInt("postId"));
                return comment;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du commentaire par contenu : " + e.getMessage());
        }
        return null; // Retourne null si aucun commentaire n'est trouvé
    }

    public List<Interaction> getCommentsByPostId(int postId) {
        List<Interaction> result = new ArrayList<>();
        String requete = "SELECT * FROM interaction WHERE postId = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, postId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Interaction t = new Interaction();
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

    public List<Interaction> getCommentsWithBadWords() {
        List<Interaction> badComments = new ArrayList<>();
        String query = "SELECT * FROM interaction WHERE content LIKE ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            for (String badWord : textFilterService.getBadWords()) {
                pst.setString(1, "%" + badWord + "%");
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Interaction comment = new Interaction();
                    comment.setId(rs.getInt("id"));
                    comment.setContent(rs.getString("content"));
                    comment.setPostId(rs.getInt("postId"));
                    badComments.add(comment);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des commentaires contenant des mots inappropriés : " + e.getMessage());
        }
        return badComments;
    }
}