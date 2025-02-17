package entites;

public class Recompense {
    public int id;
    public float reduction;
    public int idOP;
    public int idUser;


    public Recompense(int id, float reduction, int idOP, int idUser) {
        this.id = id;
        this.reduction = reduction;
        this.idOP = idOP;
        this.idUser = idUser;
    }

    public Recompense() {
    }


    public int getId() {
        return id;
    }

    public float getReduction() {
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

    public void setReduction(float reduction) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recompense that = (Recompense) o;
        return id == that.id &&
                Float.compare(that.reduction, reduction) == 0 &&
                idOP == that.idOP &&
                idUser == that.idUser;
    }

}