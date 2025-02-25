package edu.unidrive.Iservices;

import edu.unidrive.entities.Objet;

import java.sql.SQLException;
import java.util.List;

public interface ObjetPerduInterface {


        void ajouterObjet(Objet op);

        void modifierObjet(int id, Objet op);

        void supprimerObjet(int id);

        List<Objet> afficherObjets();

        Objet trouverObjet(int id);
        int updateEtat(int id, String status) throws SQLException;
    }