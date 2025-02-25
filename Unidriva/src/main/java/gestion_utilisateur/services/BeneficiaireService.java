package gestion_utilisateur.services;

import gestion_utilisateur.entities.Beneficiaire;
import gestion_utilisateur.interfaces.Iservice;
import gestion_utilisateur.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeneficiaireService implements Iservice<Beneficiaire> {

    private Connection cnx;

    public BeneficiaireService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void addEntity(Beneficiaire beneficiaire) {
        String requete = "INSERT INTO beneficiaire (nom, prenom, age, adresse, telephone, email, aide_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, beneficiaire.getNom());
            pst.setString(2, beneficiaire.getPrenom());
            pst.setInt(3, beneficiaire.getAge());
            pst.setString(4, beneficiaire.getAdresse());
            pst.setString(5, beneficiaire.getTelephone());
            pst.setString(6, beneficiaire.getEmail());
            pst.setInt(7, beneficiaire.getAideId());  // Set the aide_id

            pst.executeUpdate();
            System.out.println("Bénéficiaire ajouté avec succès!");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
        }
    }


    @Override
    public void deleteEntity(int id, Beneficiaire beneficiaire) {
        String requete = "DELETE FROM beneficiaire WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bénéficiaire supprimé avec succès!");
            } else {
                System.out.println("Aucun bénéficiaire trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(int id, Beneficiaire beneficiaire) {
        String requete = "UPDATE beneficiaire SET nom = ?, prenom = ?, age = ?, adresse = ?, telephone = ?, email = ?, aide_id = ? WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, beneficiaire.getNom());
            pst.setString(2, beneficiaire.getPrenom());
            pst.setInt(3, beneficiaire.getAge());
            pst.setString(4, beneficiaire.getAdresse());
            pst.setString(5, beneficiaire.getTelephone());
            pst.setString(6, beneficiaire.getEmail());

            // Correctly handle aide_id (can be NULL)
            if (beneficiaire.getAideId() == null) {
                pst.setNull(7, java.sql.Types.INTEGER);
            } else {
                pst.setInt(7, beneficiaire.getAideId());
            }

            pst.setInt(8, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bénéficiaire mis à jour avec succès!");
            } else {
                System.out.println("Aucun bénéficiaire trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }




    @Override
    public List<Beneficiaire> getallData() {
        List<Beneficiaire> result = new ArrayList<>();
        String requete = "SELECT * FROM beneficiaire";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(requete)) {

            while (rs.next()) {
                Beneficiaire b = new Beneficiaire();
                b.setId(rs.getInt("id"));
                b.setNom(rs.getString("nom"));
                b.setPrenom(rs.getString("prenom"));
                b.setAge(rs.getInt("age"));
                b.setAdresse(rs.getString("adresse"));
                b.setTelephone(rs.getString("telephone"));
                b.setEmail(rs.getString("email"));
                b.setAideId(rs.getInt("aide_id"));  // Retrieve the aide_id
                result.add(b);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des données: " + e.getMessage());
        }
        return result;
    }

}
