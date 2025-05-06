package edu.unidrive.services;

import edu.unidrive.entities.Interaction;
import edu.unidrive.entities.Post;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService implements Iservice<Post> {
    private final TextFilterService textFilterService = new TextFilterService(); // Ajoutez cette ligne

    private final Connection cnx = MyConnection.getInstance().getCnx(); // Singleton

    public List<String> getInterestedUsers(String postTitle) {
        List<String> interestedUsers = new ArrayList<>();
        String query = "SELECT u.email FROM utilisateur u JOIN user_interests ui ON u.id = ui.user_id WHERE ui.interest LIKE ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, "%" + postTitle + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                interestedUsers.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des utilisateurs intéressés : " + e.getMessage());
        }
        return interestedUsers;
    }

    @Override
    public void addEntity(Post entity) {
        try {
            String requete = "INSERT INTO `post`(`title`, `description`) VALUES (?,?)";
            PreparedStatement pst = cnx.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, entity.getTitle());
            pst.setString(2, entity.getDescription());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(1));
            }
            System.out.println("Success!");

            List<String> interestedUsers = getInterestedUsers(entity.getTitle());
            edu.unidrive.services.EmailPService emailPService = new edu.unidrive.services.EmailPService();
            for (String userEmail : interestedUsers) {
                emailPService.sendEmail(userEmail, "Nouveau post ajouté", "Un nouveau post a été ajouté: " + entity.getTitle());
            }

        } catch (SQLException e) {
            throw new RuntimeException("error" + e.getMessage());
        }
    }

    @Override
    public void deleteEntity(int id, Post post) {

    }

    @Override
    public void updateEntity(int id, Post post) {

    }

    @Override
    public List<Post> getallData() {
        return List.of();
    }

    @Override
    public Post getEntity(int id) {
        return null;
    }

    @Override
    public void deleteEntity(Post post) {

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
            pst.setString(1, entity.getTitle());
            pst.setString(2, entity.getDescription());
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
    public void add(Post entity) {

    }

    @Override
    public void remove(Post entity) {

    }

    @Override
    public void update(Post entity) {

    }

    @Override
    public List<Post> getAllData() {
        List<Post> result = new ArrayList<>();
        String requete = "SELECT * FROM post";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);
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
        List<Post> posts = getAllData();
        for (Post post : posts) {
            if (post.getId() == id) {
                return post;
            }
        }
        return null;
    }
    public Post getPostByTitle(String title) {
        String query = "SELECT * FROM post WHERE title = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, title);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setDescription(rs.getString("description"));
                return post;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du post par titre : " + e.getMessage());
        }
        return null; // Retourne null si aucun post n'est trouvé
    }
    public void saveUserInterests(int userId, String centresInteret) {
        String[] interests = centresInteret.split(",");
        String query = "INSERT INTO user_interests (user_id, interest) VALUES (?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            for (String interest : interests) {
                pst.setInt(1, userId);
                pst.setString(2, interest.trim());
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement des centres d'intérêt : " + e.getMessage());
        }
    }
    public boolean hasUserInterests(int userId) {
        String query = "SELECT COUNT(*) FROM user_interests WHERE user_id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification des centres d'intérêt : " + e.getMessage());
        }
        return false;
    }

    public List<Interaction> getCommentsByPostId(int postId) {
        List<Interaction> comments = new ArrayList<>();
        String query = "SELECT * FROM interaction WHERE postId = ?";
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



    public List<Post> getPostsWithBadWords() {
        List<Post> badPosts = new ArrayList<>();
        String query = "SELECT * FROM post WHERE title REGEXP '[\\*]{3}' OR description REGEXP '[\\*]{3}'";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setDescription(rs.getString("description"));
                badPosts.add(post);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des posts inappropriés: " + e.getMessage());
        }
        return badPosts;
    }
}