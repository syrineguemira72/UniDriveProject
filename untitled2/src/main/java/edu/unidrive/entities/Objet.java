package edu.unidrive.entities;

   public class Objet {


   public int id;
   public String nom ;
   public int idP ;
   public String lieuP;
       public String date;
       public String status;

public int idT;

       public Objet(int id, String nom, int idP, String lieuP, String date,String status) {
           this.id = id;
           this.nom = nom;
           this.idP = idP;
           this.lieuP = lieuP;
           this.date = date;
           this.status=status;
       }
       public Objet(String nom, int idP, String lieuP, String date,String status) {
           this.nom = nom;
           this.idP = idP;
           this.lieuP = lieuP;
           this.date = date;
           this.status=status;

       }

   public Objet() {}

       public Objet(String id) {

       }


       public int getId() {
           return this.id;
       }
       public String getStatus() {
           return this.status;
       }

   public String getNom() {
       return nom;
   }

   public int getIdP() {
       return idP;
   }

   public String getLieuP() {
       return lieuP;
   }

   public String getDate() {
       return date;
   }

       public int getIdT() {
           return idT;
       }

       public void setIdT(int idT) {
           this.idT = idT;
       }

       public void setId(int id) {
           this.id = id;
       }
       public void setStatus(String status) {
           this.status = status;
       }

   public void setNom(String nom) {
       this.nom = nom;
   }

   public void setIdP(int idP) {
       this.idP = idP;
   }

   public void setLieuP(String lieuP) {
       this.lieuP = lieuP;
   }

   public void setDate(String date) {
       this.date = date;
   }


   @Override
   public String toString() {
       return "Objet{" +
               "id=" + id +
               ", nom='" + nom + '\'' +
               ", idP=" + idP +
               ", lieuP='" + lieuP + '\'' +
               ", date=" + date +
               ", idT=" + idT +
               '}';
   }

   @Override
   public boolean equals(Object o) {
       if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;
       Objet objet = (Objet) o;
       return id == objet.id &&
               idP == objet.idP &&
               date == objet.date;
   }



}

