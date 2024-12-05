package Projet;

//package Projet.command;

import Projet.*;

public class Expansion extends Commande {
    public Expansion(Strategie strategie) {
        super(strategie);
    }

    @Override
    public void execute(Joueur joueur) {
    	System.out.println(joueur.getNom() + " effectue une commande d'expansion.");
    }
}
