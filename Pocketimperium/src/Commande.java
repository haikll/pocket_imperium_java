package Projet;

public abstract class Commande {
    private Strategie strategie;

    public Commande(Strategie strategie) {
        this.strategie = strategie;
    }

    public abstract void execute(Joueur joueur);
}
