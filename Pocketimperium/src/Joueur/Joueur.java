package Joueur;

import Carte.Commande;
import java.util.List;
import Secteur.*;
import Carte.Strategie;

public class Joueur {
    private String nom;
    private String couleur;
    private int vaisseauxPossedes;
    private List<Commande> command;
    private Secteur[] secteursControles;

    public Commande selectionnerCommande() {
        // Logic to select a command
        return null;
    }

    public void setStrategie(Strategie strategie) {
        // Logic to set strategy
    }

    public void effectuerAction() {
        // Logic to perform an action
    }

    public void placerVaisseaux() {
        // Logic to place spaceships
    }
}
