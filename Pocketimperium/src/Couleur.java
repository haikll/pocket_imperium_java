package Projet;

public enum Couleur {
    BLEU("b"),
    ROUGE("r"),
    JAUNE("j");

    private final String code;

    Couleur(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Couleur fromCode(String code) {
        for (Couleur couleur : values()) {
            if (couleur.getCode().equalsIgnoreCase(code)) {
                return couleur;
            }
        }
        throw new IllegalArgumentException("Couleur invalide");
    }
}

