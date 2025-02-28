package entites;

public class Recompense {
    public int id;
    public double reduction;
    public int idOP;
    public int idUser;


    public Recompense(int id, double reduction, int idOP, int idUser) {
        this.id = id;
        this.reduction = reduction;
        this.idOP = idOP;
        this.idUser = idUser;
    }
    public Recompense(double reduction, int idOP, int idUser) {
         this.reduction = reduction;
        this.idOP = idOP;
        this.idUser = idUser;
    }

    public Recompense() {
    }


    public int getId() {
        return id;
    }

    public double getReduction() {
        return reduction;
    }

    public int getIdOP() {
        return idOP;
    }

    public int getIdUser() {
        return idUser;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    public void setIdOP(int idOP) {
        this.idOP = idOP;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


    @Override
    public String toString() {
        return "Recompense{" +
                "id=" + id +
                ", reduction=" + reduction +
                ", idOP=" + idOP +
                ", idUser=" + idUser +
                '}';
    }




}