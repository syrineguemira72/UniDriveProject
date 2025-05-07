package edu.unidrive.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import edu.unidrive.entities.aide;
import edu.unidrive.interfaces.Iservice;
import edu.unidrive.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AideService implements Iservice<aide> {

    private final Connection cnx;

    public AideService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(aide entity) {
        addEntity(entity);
    }

    @Override
    public void remove(aide entity) {
        deleteEntity(entity.getId(), entity);
    }

    @Override
    public void update(aide entity) {
        updateEntity(entity.getId(), entity);
    }

    @Override
    public List<aide> getAllData() {
        return getallData();
    }

    @Override
    public void removeEntity(aide entity) {
        deleteEntity(entity.getId(), entity);
    }

    @Override
    public void updateEntity(aide entity) {
        updateEntity(entity.getId(), entity);
    }

    @Override
    public void addEntity(aide aide) {
        String requete = "INSERT INTO aide (currency, description, montant, created_at, id_user, association_id) "
                + "VALUES (?, ?, ?, NOW(), ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, aide.getCurrency());
            pst.setString(2, aide.getDescription());
            pst.setString(3, aide.getMontant());
            pst.setInt(4, aide.getIdUser());
            pst.setInt(5, aide.getAssociationId());
            pst.executeUpdate();
            System.out.println("Aide ajoutée avec succès!");

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    aide.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @Override
    public void deleteEntity(int id, aide aide) {
        String requete = "DELETE FROM aide WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aide supprimée avec succès!");
            } else {
                System.out.println("Aucune aide trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    @Override
    public void deleteEntity(aide aide) {
        // Implementation for abstract deleteEntity(T t)
        deleteEntity(aide.getId(), aide);
    }

    @Override
    public void updateEntity(int id, aide aide) {
        String requete = "UPDATE aide SET currency = ?, description = ?, montant = ?, id_user = ?, association_id = ? WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, aide.getCurrency());
            pst.setString(2, aide.getDescription());
            pst.setString(3, aide.getMontant());
            pst.setInt(4, aide.getIdUser());
            pst.setInt(5, aide.getAssociationId());
            pst.setInt(6, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aide mise à jour avec succès!");
            } else {
                System.out.println("Aucune aide trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    @Override
    public List<aide> getallData() {
        List<aide> result = new ArrayList<>();
        String requete = "SELECT * FROM aide";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(requete)) {

            while (rs.next()) {
                aide p = new aide();
                p.setId(rs.getInt("id"));
                p.setCurrency(rs.getString("currency"));
                p.setDescription(rs.getString("description"));
                p.setMontant(rs.getString("montant"));
                p.setCreatedAt(rs.getString("created_at"));
                p.setIdUser(rs.getInt("id_user"));
                p.setAssociationId(rs.getInt("association_id"));
                result.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des données: " + e.getMessage());
        }
        return result;
    }

    @Override
    public aide getEntity(int id) {
        String requete = "SELECT * FROM aide WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    aide p = new aide();
                    p.setId(rs.getInt("id"));
                    p.setCurrency(rs.getString("currency"));
                    p.setDescription(rs.getString("description"));
                    p.setMontant(rs.getString("montant"));
                    p.setCreatedAt(rs.getString("created_at"));
                    p.setIdUser(rs.getInt("id_user"));
                    p.setAssociationId(rs.getInt("association_id"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'entité: " + e.getMessage());
        }
        return null;
    }

    // Stripe config
    static {
        Stripe.apiKey = "sk_test_51Qx9PzQMMc132QGJ0mcQXGv3GNdLd8aULDN3CuHZLLQM58oBsDL4QWcQjZo5F3PwVEeUcc5l82x2QSmWyckQNL7d0028c0fDIu";
    }

    public boolean payerAvecStripe(double montant, String token) {
        try {
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (montant * 100));
            chargeParams.put("currency", "eur");
            chargeParams.put("source", token);
            chargeParams.put("description", "Paiement aide sociale");

            Charge charge = Charge.create(chargeParams);
            if (charge.getPaid()) {
                System.out.println("Paiement réussi!");
                return true;
            } else {
                System.out.println("Échec du paiement.");
                return false;
            }
        } catch (StripeException e) {
            e.printStackTrace();
            return false;
        }
    }
}
