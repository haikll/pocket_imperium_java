package Jeu;
import Joueur.Joueur;
import Carte.Plateau;
import Carte.Commande;


public class Jeu {
    private int tours;
    private Joueur joueurActuel;
    private Joueur[] ordreTour;
    private Plateau plateau;
    private Commande[] commandes;

    public void ajouteJoueur(Joueur joueur) {
        // Logic to add a player
    }

    public void startJeu() {
        // Logic to start the game
    }

    public void notifJoueur(Joueur joueur) {
        // Notify the player
    }

    public boolean roundTermine() {
        // Check if the round is finished
        return false;
    }

    public void checkRound() {
        // Logic to check the round
    }

    public int compterVaisseau() {
        // Logic to count spaceships
        return 0;
    }

    public void declarerGagnant(Joueur joueur) {
        // Logic to declare the winner
    }

    public void nextRound() {
        // Logic to move to the next round
    }
}
