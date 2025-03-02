package edu.unidrive.controllers;

import org.mindrot.jbcrypt.BCrypt;


public class Password {
    public static void main(String[] args) {
        String plainPassword = "admin123";
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        System.out.println("Mot de passe haché : " + hashedPassword);
    }
}