package Projet;

//package Projet.command;

import Projet.*;

public class Exploration extends Commande {
    public Exploration(Strategie strategie) {
        super(strategie);
    }

    @Override
    public void execute(Joueur joueur) {
    	System.out.println(joueur.getNom() + " effectue une commande d'exploration.");
    }
}
