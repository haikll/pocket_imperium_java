package Projet;

public class Vaisseau {
    private String couleur;
    private Joueur proprietaire;
    private Secteur emplacement;

    public Vaisseau(String couleur, Joueur proprietaire) {
        this.couleur = couleur;
        this.proprietaire = proprietaire;
    }

    public void deplacerVers(Secteur secteur) {
        this.emplacement = secteur;
    }

    public void engagerCombat(Secteur secteur) {
        // Combat logic
    }
}
