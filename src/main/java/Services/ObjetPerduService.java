package Services;

import Iservices.ObjetPerduInterface;
import entites.Objet;
import tools.MyConnection;

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
            String requete = "INSERT INTO `objets_perdu`(`nom`, `idP`, `LieuP`, `Date` ,`status`) " +
                    "VALUES (?, ?, ?, ?,?)";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

            // Setting values using PreparedStatement
            pst.setString(1, op.getNom());
            pst.setInt(2, op.getIdP());    // Set the idP
            pst.setString(3, op.getLieuP()); // Set the lieuP
            pst.setString(4, op.getDate());   // Set the date

            pst.setString(5, op.getStatus());   // Set the date

            // Execute the update
            pst.executeUpdate();

            System.out.println("objet ajouté aves succés");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifierObjet(int id, Objet op) {
        try {
            String req = "UPDATE `objets_perdu` SET `nom`=?,  `idP`=?," +
                    " `lieuP`=?, `date`=? ,`status`=? WHERE `id`=?";
            PreparedStatement pre = cnx.prepareStatement(req);

            pre.setString(1, op.getNom()); // Set the nom
            pre.setInt(2, op.getIdP());    // Set the idP
            pre.setString(3, op.getLieuP()); // Set the lieuP
            pre.setString(4, op.getDate());   // Set the date
            pre.setString(5, op.getStatus());   // Set the date
            pre.setInt(6, id);

            pre.executeUpdate();
            System.out.println("reclamation Modfié !");
        } catch(SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void supprimerObjet(int id) {
        try {
            String req = "DELETE FROM `objets_perdu` WHERE id="+id+"";
            PreparedStatement pst = cnx.prepareStatement(req);

            pst.executeUpdate();
            System.out.println("objets_perdu Supprimé !");
        } catch(SQLException ex) {
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
                list.add(new Objet(rs.getInt("id"), rs.getString("nom"), rs.getInt("idP"), rs.getString("lieuP"), rs.getString("date"), rs.getString("status")));

            }
        } catch(SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;
    }

    @Override
    public Objet trouverObjet(int id) {
        Objet o = null;
         try {
            String requete = "SELECT * FROM objets_perdu where id="+id+"";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                 o =  new  Objet(rs.getInt("id"), rs.getString("nom"), rs.getInt("idP"), rs.getString("lieuP"), rs.getString("date"), rs.getString("status"));
            }
        } catch(SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return o;    }

    @Override
    public int updateEtat(int id, String status) throws SQLException {
        String requestUpdate = "UPDATE `objets_perdu` SET `status`=?   WHERE `id`=?";
        PreparedStatement pst = cnx.prepareStatement(requestUpdate);

        pst.setString(1, "trouvé");
        pst.setInt(2, id);
         return pst.executeUpdate();
    }


    public void addEntity(Objet op) {

    }
}
