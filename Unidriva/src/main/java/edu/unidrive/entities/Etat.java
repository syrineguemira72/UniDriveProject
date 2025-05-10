package edu.unidrive.entities;

public enum Etat {
    pending,
    confirmed,
    cancelled;

    public String getLabel() {
        switch(this) {
            case pending: return "En attente";
            case confirmed: return "Confirmé";
            case cancelled: return "Annulé";
            default: throw new IllegalArgumentException();
        }
    }
}