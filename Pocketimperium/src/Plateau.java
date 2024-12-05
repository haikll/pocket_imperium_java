package Projet;

import java.util.ArrayList;
import java.util.List;

public class Plateau {
    private static Plateau instance;
    private List<Secteur> secteurs;
    public static final int WIDTH = 3;
    public static final int HEIGHT = 3;

    private Plateau() {
        this.secteurs = new ArrayList<>();
    }

    public static Plateau getInstance() {
        if (instance == null) {
            instance = new Plateau();
        }
        return instance;
    }

    public void initPlateau() {
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            secteurs.add(new Secteur());
        }
        TriPrime triPrime = new TriPrime();
        secteurs.set(4, triPrime); // Place TriPrime in the center
    }
}
