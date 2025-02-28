package edu.unidrive.interfaces;

import edu.unidrive.entities.Recompense;

import java.util.List;

public interface IRecompenseService {
    void ajouterRecompense(Recompense recompense);

    void modifierRecompense(int id, Recompense recompense);

    void supprimerRecompense(int id);


    List<Recompense> afficherRecompenses(); // Afficher toutes les récompenses

}
