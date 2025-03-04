package edu.unidrive.services;

import java.util.Arrays;
import java.util.List;

public class TextFilterService {

    // Liste des mots inappropriés
    private static final List<String> BAD_WORDS = Arrays.asList(
            "idiot", "hell", "duncky" // Remplacez par vos mots inappropriés
    );

    // Méthode pour filtrer les mots inappropriés
    public String filterBadWords(String text) {
        if (text == null || text.isEmpty()) {
            return text; // Retourne le texte tel quel s'il est vide
        }

        // Parcourir la liste des mots inappropriés
        for (String badWord : BAD_WORDS) {
            // Remplacer chaque occurrence du mot inapproprié par des astérisques
            text = text.replaceAll("(?i)\\b" + badWord + "\\b", "***");
        }

        return text;
    }
    public List<String> getBadWords() {
        return BAD_WORDS;
    }
}