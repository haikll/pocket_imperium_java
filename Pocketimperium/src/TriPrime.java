package Projet;

import java.util.List;

public class TriPrime extends Secteur {
    private List<Secteur> secteurs;

    public TriPrime() {
        super();
        // Initialize as a special sector
    }

    public boolean isTriPrimePos(Secteur secteur) {
        // Check if the position is part of TriPrime
        return secteurs.contains(secteur);
    }
}
