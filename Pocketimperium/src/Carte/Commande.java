package Carte;
import Joueur.Joueur;

public class Commande {
    private TypeCommande type;
    private Strategie strategie;

    public Commande(TypeCommande type, Strategie strategie) {
        this.type = type;
        this.strategie = strategie;
    }

    public TypeCommande getType() {
        return type;
    }

    public Strategie getStrategie() {
        return strategie;
    }

    public void execute(Joueur joueur) {
        strategie.execute(joueur);
    }
}
