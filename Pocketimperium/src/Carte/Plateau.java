package Jeu;

import java.util.List;

public class Plateau {
    private static Plateau instance;
    private List<Secteur> secteur;
    private static final int HEIGHT = 0; // Placeholder value
    private static final int WIDTH = 0; // Placeholder value

    private Plateau() {
        // Constructor logic
    }

    public static Plateau initPlateau() {
        if (instance == null) {
            instance = new Plateau();
        }
        return instance;
    }
}
