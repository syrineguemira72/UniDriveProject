package ACT.entities;

public class Participant {
    private int id;
    private String nom;
    private String email;
    private EtatParticipant etat; // Enum pour représenter l'état du participant

    // Enum interne pour gérer les états
    public enum EtatParticipant {
        CONDUCTEUR, ETUDIANT
    }

    // Constructeurs
    public Participant() {}

    public Participant(int id, String nom, String email, EtatParticipant etat) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.etat = etat;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public EtatParticipant getEtat() { return etat; }
    public void setEtat(EtatParticipant etat) { this.etat = etat; }

    // Méthode toString pour affichage
    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", etat=" + etat +
                '}';
    }
}
