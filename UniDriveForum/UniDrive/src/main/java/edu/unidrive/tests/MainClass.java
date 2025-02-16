package edu.unidrive.tests;

import edu.unidrive.entities.Profile;
import edu.unidrive.entities.Utilisateur;
import edu.unidrive.services.ProfileService;
import edu.unidrive.services.UserService;
import edu.unidrive.tools.MyConnection;

public class MainClass {
    public static void main(String[] args) {
        // Initialiser la connexion
        MyConnection mc = new MyConnection();

        // Créer un service utilisateur
        UserService us = new UserService();

        // Ajouter un utilisateur
        Utilisateur user = new Utilisateur(1, "Belhadj", "Rihab", "rihab.belhadj@gmail.com", "123", "admin");
       // us.add(user);

        /* Modifier l'utilisateur
        user.setNom("Belhadj Updated");
        user.setEmail("rihab.updated@gmail.com");
        us.update(user);*/

        // Supprimer l'utilisateur
        // us.remove(user);

        // Créer un service profile
        ProfileService ps = new ProfileService();

        // Ajouter un utilisateur
        Profile p = new Profile(1,"image","car","21564789","mourouj");
        // ps.add(p);

        /*Modifier profile
        p.setAdresse("mourouj Updated");
        ps.update(p);*/

        // Supprimer l'utilisateur
        ps.remove(p);


    }
}
