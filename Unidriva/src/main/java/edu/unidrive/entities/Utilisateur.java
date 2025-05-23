package edu.unidrive.entities;

public class Utilisateur {

    public static final String ROLE_PASSAGER = "PASSAGER";
    public static final String ROLE_CONDUCTEUR = "CONDUCTEUR";

    private int id;
    private String email;
    private String dob;
    private String gender;
    private String lastname;
    private String firstname;
    private String password;
    private String role; // Nouvel attribut pour le rôle
    private Profile profile;

    public Utilisateur() {
    }

    public Utilisateur(int id, String email, String dob, String gender, String lastname, String firstname, String password, String role) {
        this.id = id;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.lastname = lastname;
        this.firstname = firstname;
        this.password = password;
        this.role = role;
    }

    public Utilisateur(String email, String dob, String gender, String lastname, String firstname, String password, String role) {
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.lastname = lastname;
        this.firstname = firstname;
        this.password = password;
        this.role = role;
    }

    // Getters et setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", profile=" + profile +
                '}';
    }
}