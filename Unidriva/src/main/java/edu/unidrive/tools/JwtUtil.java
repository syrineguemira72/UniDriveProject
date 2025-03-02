package edu.unidrive.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Clé secrète pour signer le JWT
    private static final long EXPIRATION_TIME = 86400000; // 24 heures en millisecondes

    // Générer un JWT
    public static String generateToken(String email, String role) {
        try {
            return Jwts.builder()
                    .setSubject(email)
                    .claim("role", role) // Ajouter le rôle comme claim
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SECRET_KEY)
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retourner null en cas d'erreur
        }
    }

    // Valider un JWT et extraire les informations
    public static Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retourner null en cas d'erreur
        }
    }
}