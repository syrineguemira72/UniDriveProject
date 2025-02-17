package Services;

import Iservices.IRecompenseService;
import entites.Recompense;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecompenseService implements IRecompenseService {

    Connection cnx = MyConnection.getInstance().getCnx();

    @Override
    public void ajouterRecompense(Recompense recompense) {
        try {
            String requete = "INSERT INTO `recompense`( `idOP`, `Reduction`, `IdUser`) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

             pst.setInt(1, recompense.getIdOP());
            pst.setFloat(2, recompense.getReduction());
            pst.setInt(3, recompense.getIdUser());

            // Execute the update
            pst.executeUpdate();
            System.out.println("recompense ajoutée aves succés");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifierRecompense(int id, Recompense recompense) {
        try {
            String req = "UPDATE `recompense` SET `idOP`=?," +
                    " `idUser`=?, `reduction`=? WHERE `id`=?";
            PreparedStatement pre = cnx.prepareStatement(req);

             pre.setInt(1, recompense.getIdOP());
            pre.setFloat(2, recompense.getReduction());
            pre.setInt(3, recompense.getIdUser());
            pre.setInt(4, id);
            pre.executeUpdate();
            System.out.println("recompense Modfié !");
        } catch(SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void supprimerRecompense(int id) {
            try {
                String req = "DELETE FROM `recompense` WHERE id="+id+"";
                PreparedStatement pst = cnx.prepareStatement(req);

                pst.executeUpdate();
                System.out.println("recompense Supprimé !");
            } catch(SQLException ex) {
                System.err.println(ex.getMessage());
            }
    }

    @Override
    public List<Recompense> afficherRecompenses() {

        List<Recompense> list = new ArrayList<>();
        try {
            String requete = "SELECT * FROM Recompense";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                list.add(new Recompense(rs.getInt("id"), rs.getFloat("reduction"), rs.getInt("idOP"), rs.getInt("idUser")));

            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return list;
    }
}
