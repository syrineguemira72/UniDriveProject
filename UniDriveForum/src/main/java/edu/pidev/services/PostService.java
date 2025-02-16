package edu.pidev.services;

import edu.pidev.entities.Interaction;
import edu.pidev.entities.Post;
import edu.pidev.interfaces.Iservice;
import edu.pidev.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService implements Iservice <Post> {

    private final Connection cnx = MyConnection.getInstance().getCnx();//singleton


    @Override
    public void addEntity(Post entity) {
        try {

            String requete = " INSERT INTO `post`( `title`, `description`) VALUES (?,?)" ;
            PreparedStatement pst = cnx.prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, entity.getTitle());
            pst.setString(2, entity.getDescription());
            pst.executeUpdate();
            // Récupérer l'ID généré
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(1));
            }
            System.out.println("Success!");
        } catch (SQLException e) {
            throw new RuntimeException("error"+e.getMessage());
        }
    }



    @Override
    public void removeEntity(Post entity) {
        String requete = "DELETE FROM `post` WHERE id = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, entity.getId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post supprimé avec succès !");
            } else {
                System.out.println("Aucun post trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du post : " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(Post entity) {
        String requete = "UPDATE `post` SET `title` = ?, `description` = ? WHERE `id` = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, entity.getTitle()); // Met à jour le titre
            pst.setString(2, entity.getDescription()); // Met à jour la description
            pst.setInt(3, entity.getId()); // Identifie le post à mettre à jour avec l'ID

            int rowsAffected = pst.executeUpdate(); // Exécute la mise à jour
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
    public List<Post> getAllData() {
        List<Post> result = new ArrayList<>();
        String requete = "SELECT * FROM post";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs=st.executeQuery(requete);
            while (rs.next()) {
                Post p = new Post();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString(3));
                result.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
    public Post getById(int id) {
        List<Post> posts = getAllData(); // Récupérer tous les posts
        for (Post post : posts) {
            if (post.getId() == id) {
                return post; // Retourne le post correspondant à l'ID
            }
        }
        return null; // Retourne null si aucun post trouvé
    }
    public List<Interaction> getCommentsByPostId(int postId) {
        List<Interaction> comments = new ArrayList<>();
        String query = "SELECT * FROM interaction WHERE postId = ?"; // Assurez-vous que la table s'appelle bien "interaction"
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, postId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Interaction interaction = new Interaction();
                interaction.setId(rs.getInt("id"));
                interaction.setContent(rs.getString("content"));
                interaction.setDate(rs.getDate("date").toLocalDate());
                interaction.setPostId(rs.getInt("postId"));
                comments.add(interaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des commentaires : " + e.getMessage());
        }
        return comments;
    }
}
