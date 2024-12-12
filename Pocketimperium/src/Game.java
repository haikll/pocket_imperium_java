package ProjetLatest;
import java.util.*;

class Game {
    private List<Player> players;
    private Board board;
    private int round;

    public void startGame() {
        setupGame();
        initialShipDeployment();
        while (!isGameOver()) {
            playRound();
        }
        determineWinner();
    }

    private void setupGame() {
        board = new Board();
        board.setup();
        players = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        List<String> chosenColors = new ArrayList<>();
        String[] availableColors = {"Red", "Blue", "Yellow"};
        for (int i = 1; i <= 3; i++) {
            System.out.println("Enter name for Player " + i + " (or type 'AI' for a virtual player): ");
            String name = scanner.nextLine();
            String color = "";
            do {
                System.out.println("Choose a color (available: " + String.join(", ", availableColors) + "): ");
                color = scanner.nextLine();
                if (chosenColors.contains(color)) {
                    System.out.println("Color already chosen. Please select a different color.");
                }
            } while (chosenColors.contains(color));
            chosenColors.add(color);
            if (name.equalsIgnoreCase("AI")) {
                players.add(new Player("AI Player " + i, true, new AggressiveStrategy()));
            } else {
                players.add(new Player(name, false));
            }
        }
        round = 1;
    }

    private void initialShipDeployment() {
        System.out.println("Initial ship deployment phase.");
        System.out.println("Beginning with the start player and going clockwise,");
        System.out.println("each player places 2 of their ships on a single,");
        System.out.println("unoccupied Level I system in an unoccupied sector.");
        System.out.println("Then, starting with the last player and going anticlockwise,");
        System.out.println("each player repeats this action, placing 2 more of their ships on another unoccupied Level I system in an unoccupied sector.");

        // Deploy in clockwise order
        for (Player player : players) {
            deployShips(player);
        }

        // Deploy in anticlockwise order
        for (int i = players.size() - 1; i >= 0; i--) {
            deployShips(players.get(i));
        }
    }

    private void deployShips(Player player) {
        Scanner scanner = new Scanner(System.in);
        List<Sector> availableSectors = board.getSectors();
        System.out.println(player.getName() + ", choose a sector to place 2 ships:");
        for (int i = 0; i < availableSectors.size(); i++) {
            Sector sector = availableSectors.get(i);
            if (sector.getLevel() == 1 && sector.getOwner() == null) { // Only Level 1
                System.out.println(i + ": Level " + sector.getLevel() + ", Unoccupied");
            }
        }
        int sectorIndex;
        do {
            System.out.println("Enter the sector index:");
            sectorIndex = scanner.nextInt();
            if (sectorIndex < 0 || sectorIndex >= availableSectors.size() || 
                availableSectors.get(sectorIndex).getOwner() != null || 
                availableSectors.get(sectorIndex).getLevel() != 1) {
                System.out.println("Invalid selection. Try again.");
            }
        } while (sectorIndex < 0 || sectorIndex >= availableSectors.size() || 
                 availableSectors.get(sectorIndex).getOwner() != null || 
                 availableSectors.get(sectorIndex).getLevel() != 1);

        Sector chosenSector = availableSectors.get(sectorIndex);
        chosenSector.setOwner(player);
        System.out.println(player.getName() + " placed 2 ships on sector " + sectorIndex + ".");
    }


    private void playRound() {
        System.out.println("Round " + round);
        // Phase 1: Plan
        System.out.println("Phase 1: Plan - Players choose the order of their commands.");
        for (Player player : players) {
            player.chooseCommandOrder();
        }

        // Phase 2: Perform
        System.out.println("Phase 2: Perform - Execute commands in order.");
        for (int i = 0; i < 3; i++) { // Each player has 3 commands
            System.out.println("Executing commands for step " + (i + 1));
            for (Player player : players) {
                Command command = player.getCommandAt(i); // Assume getCommandAt fetches command in chosen order
                command.execute(player, board);
            }
        }

        // Phase 3: Exploit
        System.out.println("Phase 3: Exploit - Sustain ships and score sectors.");
        for (Sector sector : board.getSectors()) {
            sector.sustainShips();
        }
        for (Player player : players) {
            System.out.println(player.getName() + " is choosing a sector to score.");
            player.scoreSector(board); // Assume this method handles scoring logic
        }

        round++;
    }

    private boolean isGameOver() {
        return round > 9;
    }

    private void determineWinner() {
        Player winner = players.stream()
            .max(Comparator.comparingInt(Player::calculateScore))
            .orElse(null);
        System.out.println("The winner is: " + winner.getName());
    }
}
