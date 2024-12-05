package Projet;

import java.util.*;

public class Jeu {
    private int tours;
    private Joueur joueurActuel;
    private List<Joueur> ordreTour = new ArrayList<>();
    private Plateau plateau;
    private List<Commande> commandes;
    private List<Joueur> joueurs;

    public Jeu() {
        this.plateau = Plateau.getInstance();
        this.commandes = new ArrayList<>();
        this.tours = 0;
        this.joueurs = new ArrayList<>();
    }

    public void ajouteJoueur() {
        Scanner scanner = new Scanner(System.in);
        List<Couleur> couleursDisponibles = new ArrayList<>();
        couleursDisponibles.add(Couleur.BLEU);
        couleursDisponibles.add(Couleur.ROUGE);
        couleursDisponibles.add(Couleur.JAUNE);

        while (!couleursDisponibles.isEmpty()) {
            System.out.println("Entrez le nom du joueur :");
            String nom = scanner.nextLine();

            System.out.println("Choisissez une couleur disponible :");
            for (Couleur couleur : couleursDisponibles) {
                System.out.println(couleur.getCode() + " pour " + couleur.name().toLowerCase());
            }

            String codeCouleur = scanner.nextLine().toLowerCase();
            try {
                Couleur couleurChoisie = Couleur.fromCode(codeCouleur);
                if (!couleursDisponibles.contains(couleurChoisie)) {
                    System.out.println("Cette couleur est déjà choisie. Essayez une autre.");
                    continue;
                }

                Joueur joueur = new Joueur(nom, couleurChoisie);
                joueurs.add(joueur);
                couleursDisponibles.remove(couleurChoisie);

                System.out.println("Joueur ajouté : " + nom + " avec la couleur " + couleurChoisie.name().toLowerCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Entrée invalide. Essayez encore.");
            }
        }

        System.out.println("Tous les joueurs sont prêts !");
    }
    
    public void ajouteJoueur(Joueur joueur) {
        joueurs.add(joueur);
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public void startJeu() {
        plateau.initPlateau();
        System.out.println("Le jeu commence !");
    }

    public void nextRound() {
        tours++;
    }

    public List<Joueur> getOrdreTour() {
        return ordreTour;
    }

    public int getTours() {
        return tours;
    }

    public void jouerTour() {
        Map<Joueur, Queue<Commande>> commandesParJoueur = new HashMap<>();

        // Phase 1: Plan (Players choose the order of their commands)
        for (Joueur joueur : ordreTour) {
            System.out.println(joueur.getNom() + ", choisissez l'ordre de vos commandes (1. Expand, 2. Explore, 3. Exterminate).");
            Scanner scanner = new Scanner(System.in);
            Queue<Commande> queue = new LinkedList<>();

            for (int i = 0; i < 3; i++) {
                int choix = scanner.nextInt();
                switch (choix) {
                    case 1:
                        queue.add(new Expansion(null));
                        break;
                    case 2:
                        queue.add(new Exploration(null));
                        break;
                    case 3:
                        queue.add(new Extermination(null));
                        break;
                    default:
                        System.out.println("Choix invalide. Réessayez.");
                        i--;
                        break;
                }
            }
            commandesParJoueur.put(joueur, queue);
        }

        // Phase 2: Perform (Resolve commands in the order: Expand, Explore, Exterminate)
        for (String typeCommande : List.of("Expand", "Explore", "Exterminate")) {
            for (Joueur joueur : ordreTour) {
                Queue<Commande> queue = commandesParJoueur.get(joueur);
                if (queue != null && !queue.isEmpty()) {
                    Commande commande = queue.peek();
                    if (commande.getClass().getSimpleName().equals(typeCommande)) {
                        queue.poll();
                        System.out.println(joueur.getNom() + " exécute " + typeCommande + ".");
                        commande.execute(joueur);
                    }
                }
            }
        }

        // Phase 3: Exploit (Score sectors and sustain ships)
        for (Joueur joueur : ordreTour) {
            System.out.println("Phase Exploit pour " + joueur.getNom());
            joueur.exploit(plateau);
        }

        nextRound();
    }
}


