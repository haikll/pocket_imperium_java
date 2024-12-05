package Projet;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private String nom;
    private Couleur couleur;
    private int vaisseauxPossedes;
    private List<Commande> commandes;
    private List<Secteur> secteursControles;

    public Joueur(String nom, Couleur couleur) {
        this.nom = nom;
        this.couleur = couleur;
        this.vaisseauxPossedes = 15; // Default starting ships
        this.commandes = new ArrayList<>();
        this.secteursControles = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public void setStrategie(Strategie strategie) {
        // Set strategy for the player
    }

    public void effectuerAction(Commande commande) {
        commande.execute(this);
    }

    public void placerVaisseaux(Secteur secteur) {
        secteur.placerVaisseau(this);
    }
    
    public void exploit(Plateau plateau) {
        System.out.println(this.nom + " évalue ses secteurs et ses vaisseaux.");

        // Sustain ships logic
        // Example: Remove ships if resources are insufficient.

        // Score sectors logic
        // Example: Calculate points for controlled sectors.
    }

}
