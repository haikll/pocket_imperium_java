package Projet;

import java.util.*;

public class Main {
	
	private static String getAvailableColors(Set<Couleur> couleursUtilisées) {
        StringBuilder availableColors = new StringBuilder();
        for (Couleur couleur : Couleur.values()) {
            if (!couleursUtilisées.contains(couleur)) {
                availableColors.append(couleur.getCode()).append(" (").append(couleur.name().toLowerCase()).append("), ");
            }
        }
        return availableColors.length() > 0 ? availableColors.substring(0, availableColors.length() - 2) : "Aucune couleur disponible";
    }
	
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Jeu jeu = new Jeu();
        Set<Couleur> couleursUtilisées = new HashSet<>();
        int nombreDeJoueurs;

        System.out.println("Bienvenue dans Pocket Imperium !");

        while (true) {
            System.out.println("Entrez le nombre de joueurs (min 2, max 3) :");
            nombreDeJoueurs = scanner.nextInt();
            scanner.nextLine(); // Consume newline left by nextInt()
            if (nombreDeJoueurs >= 2 && nombreDeJoueurs <= 3) {
                break;
            } else {
                System.out.println("Le nombre de joueurs doit être entre 2 et 3.");
            }
        }

        for (int i = 1; i <= nombreDeJoueurs; i++) {
            System.out.println("Entrez le nom du joueur " + i + " :");
            String nom = scanner.nextLine();

            Couleur couleurChoisie = null;
            while (couleurChoisie == null) {
                System.out.println("Choisissez une couleur (b pour bleu, r pour rouge, j pour jaune) pour " + nom + " :");
                System.out.println("Couleurs disponibles: " + getAvailableColors(couleursUtilisées));
                String codeCouleur = scanner.nextLine().toLowerCase();

                try {
                    couleurChoisie = Couleur.fromCode(codeCouleur);

                    // Check if the color has already been chosen
                    if (couleursUtilisées.contains(couleurChoisie)) {
                        System.out.println("Cette couleur est déjà utilisée. Choisissez-en une autre.");
                        couleurChoisie = null;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Entrée invalide. Veuillez entrer 'b', 'r', ou 'j'.");
                }
            }

            Joueur joueur = new Joueur(nom, couleurChoisie);
            jeu.ajouteJoueur(joueur);
            couleursUtilisées.add(couleurChoisie);

            System.out.println("Joueur ajouté : " + nom + " avec la couleur " + couleurChoisie.name().toLowerCase());
        }

        // Initialiser le plateau et démarrer le jeu
        jeu.startJeu();

        boolean jeuEnCours = true;

        while (jeuEnCours) {
            Joueur joueurActuel = jeu.getOrdreTour().get(jeu.getTours() % jeu.getOrdreTour().size());
            System.out.println("C'est le tour de " + joueurActuel.getNom());

            // Afficher les commandes disponibles
            System.out.println("Choisissez une action :");
            System.out.println("1. Expansion");
            System.out.println("2. Exploration");
            System.out.println("3. Extermination");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            Commande commande;
            switch (choix) {
                case 1:
                    commande = new Expansion(null); // You can implement strategies later
                    break;
                case 2:
                    commande = new Exploration(null);
                    break;
                case 3:
                    commande = new Extermination(null);
                    break;
                default:
                    System.out.println("Action invalide. Tour passé.");
                    continue;
            }

            jeu.startJeu();

            while (jeu.getTours() < 9) {
                System.out.println("\n===== Tour " + (jeu.getTours() + 1) + " =====");
                jeu.jouerTour();
            }

            System.out.println("Le jeu est terminé !");
            // Add logic to declare winner
        }

        scanner.close();
    }
}

