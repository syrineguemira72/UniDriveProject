package edu.unidrive.entities;

public class Profile {
    private int id;
    private String photo;
    private String bio;
    private String telephone;
    private String adresse;
    private Utilisateur utilisateur;



    public Profile() {
    }

    public Profile(int id, String photo, String bio, String telephone, String adresse) {
        this.id = id;
        this.photo = photo;
        this.bio = bio;
        this.telephone = telephone;
        this.adresse = adresse;
    }
    public Profile(String photo, String bio, String telephone, String adresse ) {
        this.photo = photo;
        this.bio = bio;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", photo='" + photo + '\'' +
                ", bio='" + bio + '\'' +
                ", telephone='" + telephone + '\'' +
                ", adresse='" + adresse + '\'' +
                ", utilisateur=" + utilisateur +
                '}';
    }
}
