package Projet;

//package Projet.command;

import Projet.*;

public class Extermination extends Commande {
    public Extermination(Strategie strategie) {
        super(strategie);
    }

    @Override
    public void execute(Joueur joueur) {
    	System.out.println(joueur.getNom() + " effectue une commande d'extermination.");
    }
}

