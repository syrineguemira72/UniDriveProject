package edu.unidrive.services;

import java.util.Arrays;
import java.util.List;

public class TextFilterService {

    private static final List<String> BAD_WORDS = Arrays.asList(
            "idiot", "hell", "duncky" // Remplacez par vos mots inappropriés
    );

    public String filterBadWords(String text) {
        if (text == null || text.isEmpty()) {
            return text; // Retourne le texte tel quel s'il est vide
        }

        for (String badWord : BAD_WORDS) {
            text = text.replaceAll("(?i)\\b" + badWord + "\\b", "***");
        }

        return text;
    }
    public List<String> getBadWords() {
        return BAD_WORDS;
    }
}