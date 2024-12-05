package Projet;

public class Secteur {
    private int niveauDeSysteme;
    private boolean estOccupe;
    private Joueur joueurOccupant;

    public Secteur() {
        this.niveauDeSysteme = 0; // Default level
        this.estOccupe = false;
    }

    public void placerVaisseau(Joueur joueur) {
        if (!estOccupe) {
            estOccupe = true;
            joueurOccupant = joueur;
        }
    }

    public void retirerVaisseau() {
        estOccupe = false;
        joueurOccupant = null;
    }
}
