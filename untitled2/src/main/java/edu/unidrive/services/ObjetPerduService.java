package edu.unidrive.services;

import edu.unidrive.Iservices.ObjetPerduInterface;
import edu.unidrive.entities.Objet;
import edu.unidrive.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObjetPerduService implements ObjetPerduInterface {
    Connection cnx = MyConnection.getInstance().getCnx();

    private Statement ste;
    private PreparedStatement pst;

    @Override
    public void ajouterObjet(Objet op) {
        try {
            String requete = "INSERT INTO objets_perdu (nom, status, date, description, lieu, categorie, imagePath) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

            // Setting values using PreparedStatement
            pst.setString(1, op.getNom());
            pst.setString(2, op.getStatus());
            pst.setString(3, op.getDate());
            pst.setString(4, op.getDescription());
            pst.setString(5, op.getLieu());
            pst.setString(6, op.getCategorie());
            pst.setString(7, op.getImagePath());

            // Execute the update
            pst.executeUpdate();

            System.out.println("Objet ajouté avec succès");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifierObjet(int id, Objet op) {
        try {
            String req = "UPDATE objets_perdu SET nom=?, status=?, date=?, description=?, lieu=?, categorie=?, imagePath=? WHERE id=?";
            PreparedStatement pre = cnx.prepareStatement(req);

            pre.setString(1, op.getNom());
            pre.setString(2, op.getStatus());
            pre.setString(3, op.getDate());
            pre.setString(4, op.getDescription());
            pre.setString(5, op.getLieu());
            pre.setString(6, op.getCategorie());
            pre.setString(7, op.getImagePath());
            pre.setInt(8, id);

            pre.executeUpdate();
            System.out.println("Objet modifié !");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void supprimerObjet(int id) {
        try {
            String req = "DELETE FROM objets_perdu WHERE id=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, id);

            pst.executeUpdate();
            System.out.println("Objet supprimé !");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public List<Objet> afficherObjets() {
        List<Objet> list = new ArrayList<>();
        try {
            String requete = "SELECT * FROM objets_perdu";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                list.add(new Objet(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("status"),
                        rs.getString("date"),
                        rs.getString("description"),
                        rs.getString("lieu"),
                        rs.getString("categorie"),
                        rs.getString("imagePath")
                ));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;
    }

    @Override
    public Objet trouverObjet(int id) {
        Objet o = null;
        try {
            String requete = "SELECT * FROM objets_perdu WHERE id=?";
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                o = new Objet(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("status"),
                        rs.getString("date"),
                        rs.getString("description"),
                        rs.getString("lieu"),
                        rs.getString("categorie"),
                        rs.getString("imagePath")
                );
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return o;
    }

    @Override
    public int updateEtat(int id, String status) throws SQLException {
        String requestUpdate = "UPDATE objets_perdu SET status=? WHERE id=?";
        PreparedStatement pst = cnx.prepareStatement(requestUpdate);

        pst.setString(1, status);
        pst.setInt(2, id);
        return pst.executeUpdate();
    }

    public void addEntity(Objet op) {
        ajouterObjet(op);
    }
}