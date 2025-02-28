package edu.unidrive.entities;

public enum AideType {
    ALIMENTAIRE("alimentaire"),
    FINANCIER("financier"),
    MEDICAL("médical");

    private final String type;

    AideType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static boolean isValidType(String type) {
        for (AideType aideType : AideType.values()) {
            if (aideType.getType().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}
