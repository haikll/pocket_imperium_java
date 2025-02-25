package ProjetLatest;
import java.util.*;

class Game {
    private List<Player> players;
    private Board board;
    private int round;
    private int currentStep;

    public void startGame() {
        setupGame();
        initialShipDeployment();
        while (!isGameOver()) {
            playRound();
        }
        determineWinner();
    }

    public List<Player> getPlayers() {
        return players;
    }

 // Add a getter
    public int getCurrentStep() {
        return currentStep;
    }

    private void setupGame() {
        board = new Board();
        board.setup();
        players = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        List<String> chosenColors = new ArrayList<>();
        List<String> chosenNames = new ArrayList<>();
        String[] availableColors = {"Red", "Blue", "Yellow"};

        for (int i = 1; i <= 3; i++) {
            String name;
            do {
                System.out.println("Enter name for Player " + i + " (or type 'AI' for a virtual player): ");
                name = scanner.nextLine();
                if (chosenNames.contains(name)) {
                    System.out.println("Name already taken. Please choose a different name.");
                }
            } while (chosenNames.contains(name));

            chosenNames.add(name);

            String color = "";
            while (true) {
                System.out.println("Choose a color (available: " + String.join(", ", getAvailableColors(availableColors, chosenColors)) + "): ");
                color = scanner.nextLine();
                if (!isValidColor(color, availableColors)) {
                    System.out.println("Invalid color. Please choose exactly Red, Blue, or Yellow (case-sensitive).");
                } else if (chosenColors.contains(color)) {
                    System.out.println("Color already chosen. Please select a different color.");
                } else {
                    break;
                }
            }

            chosenColors.add(color);

            if (name.equalsIgnoreCase("AI")) {
                players.add(new Player("AI Player " + i, true, new AggressiveStrategy()));
            } else {
                players.add(new Player(name, false));
            }
        }

        round = 1;
    }

    private boolean isValidColor(String color, String[] availableColors) {
        for (String validColor : availableColors) {
            if (validColor.equals(color)) { // Case-sensitive comparison
                return true;
            }
        }
        return false;
    }

    private List<String> getAvailableColors(String[] availableColors, List<String> chosenColors) {
        List<String> available = new ArrayList<>();
        for (String color : availableColors) {
            if (!chosenColors.contains(color)) {
                available.add(color);
            }
        }
        return available;
    }

    String[][] map = {
            {"II",  "",  "", "I",  "",  "I",  "",  "",  ""},
            {"",  "",  "I",   "",  "II",  "",   "II",  "I", ""},
            {"", "I",  "",   "",  "",  "",   "",  "",   "II"},
            {"",  "",  "",   "III",  "III", "III",   "",  "",   "I"},
            {"II", "I", "",   "III",  "III",  "III",   "",  "II",   ""},
            {"",  "",  "II",   "III",  "III",  "III",   "I",  "",   ""},
            {"",  "I",  "",  "",  "",  "",   "",  "II",   ""},
            {"I",  "",  "",   "I",  "",  "I", "", "", ""},
            {"",  "",  "II",   "",  "II",  "",   "I",  "",   "I"},
    };





    private void initialShipDeployment() {
        System.out.println("INITIAL SHIP DEPLOYMENT PHASE");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("1. Beginning with the start player and going clockwise,");
        System.out.println("2. Each player places 2 of their ships on a single");
        System.out.println("unoccupied Level I system in an unoccupied sector.");
        System.out.println("3. Then, starting with the last player and going anticlockwise,");
        System.out.println("each player repeats this action, placing 2 more of their ships on another unoccupied Level I system in an unoccupied sector.");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("This is the map of the galaxy that might help you :");

        // Deploy in clockwise order
        for (Player player : players) {
            deployShips(player);
        }

        // Deploy in anticlockwise order
        for (int i = players.size() - 1; i >= 0; i--) {
            deployShips(players.get(i));
        }

        for (Player player : players) {
            player.chooseCommandOrder();
        }



    }

    private void deployShips(Player player) {
        Scanner scanner = new Scanner(System.in);
        List<Sector> availableSectors = board.getSectors().stream()
            .filter(sector -> sector.getLevel() == 1 && sector.getOwner() == null)
            .toList();

        if (availableSectors.isEmpty()) {
            System.out.println("No available sectors for deployment.");
            return;
        }
        displayBoard();
        System.out.println(player.getName() + ", choose a sector to place 2 ships:");
        availableSectors.forEach(sector ->
            System.out.println(sector.getName() + ": Level " + sector.getLevel() + ", Unoccupied"));

        Sector chosenSector = null;
        while (chosenSector == null) {
            System.out.println("Enter the sector name (e.g., A1):");
            String sectorName = scanner.nextLine(); // Define sectorName inside the loop
            chosenSector = availableSectors.stream()
                .filter(sector -> sector.getName().equalsIgnoreCase(sectorName))
                .findFirst()
                .orElse(null);

            if (chosenSector == null) {
                System.out.println("Invalid selection. Try again.");
            }
        }

        for (int i = 0; i < 2; i++) {
            chosenSector.getShips().add(new Ship(player));
        }

        // Set ownership of the sector
        chosenSector.setOwner(player);
        System.out.println(player.getName() + " placed 2 ships in sector " + chosenSector.getName() + ".");
        System.out.println("-------------------------------------------------");
    }


    private void playRound() {
        System.out.println("Round " + round);
        for (int step = 0; step < 3; step++) {
            currentStep = step;
            System.out.println("Executing commands for step " + (step + 1));

            // Process commands based on priority (Expansion -> Exploration -> Extermination)
            List<Player> expansionPlayers = new ArrayList<>();
            List<Player> explorationPlayers = new ArrayList<>();
            List<Player> exterminationPlayers = new ArrayList<>();

            for (Player player : players) {
                Command command = player.getCommandAt(currentStep);
                if (command instanceof ExpandCommand) {
                    expansionPlayers.add(player);
                } else if (command instanceof ExploreCommand) {
                    explorationPlayers.add(player);
                } else if (command instanceof ExterminateCommand) {
                    exterminationPlayers.add(player);
                }
            }

            // Execute Expansion commands
            for (Player player : expansionPlayers) {
                Command command = player.getCommandAt(currentStep);
                if (command instanceof ExpandCommand) { // Check if the command is ExpandCommand
                    System.out.println(player.getName() + ", here's the current board:");
                    displayBoard(); // Display the board
                    System.out.println(player.getName() + ", do you want to execute your ExpandCommand? (y/n)");

                    Scanner scanner = new Scanner(System.in);
                    String response = scanner.nextLine();

                    if (response.equalsIgnoreCase("y")) {
                        // Execute the ExpandCommand
                        command.execute(player, board, this);
                    } else {
                        System.out.println(player.getName() + " chose not to execute the ExpandCommand.");
                    }
                }
            }

            // Execute Exploration commands
            for (Player player : explorationPlayers) {
                Command command = player.getCommandAt(currentStep);
                if (command instanceof ExploreCommand) { // Check if it's an ExploreCommand
                    System.out.println(player.getName() + ", here's the current board:");
                    displayBoard(); // Display the board
                    System.out.println(player.getName() + ", do you want to execute your ExploreCommand? (y/n)");
                    Scanner scanner = new Scanner(System.in);
                    String response = scanner.nextLine();
                    if (response.equalsIgnoreCase("y")) {
                        // Cast to ExploreCommand and execute
                        ((ExploreCommand) command).execute(player, board, this);
                    } else {
                        System.out.println(player.getName() + " chose not to execute the ExploreCommand.");
                    }
                }
            }


            // Execute Extermination commands
            for (Player player : exterminationPlayers) {
                Command command = player.getCommandAt(currentStep);
                if (command instanceof ExterminateCommand) { // Check if it's an ExterminateCommand
                    System.out.println(player.getName() + ", here's the current board:");
                    displayBoard(); // Display the board
                    System.out.println(player.getName() + ", do you want to execute your ExterminateCommand? (y/n)");
                    Scanner scanner = new Scanner(System.in);
                    String response = scanner.nextLine();
                    if (response.equalsIgnoreCase("y")) {
                        ((ExterminateCommand) command).execute(player, board, this); // Call the execute method
                    } else {
                        System.out.println(player.getName() + " chose not to execute the ExterminateCommand.");
                    }
                }
            }

        }

        System.out.println("Phase 3: Exploit - Sustain ships and score sectors.");
        for (Sector sector : board.getSectors()) {
            sector.sustainShips();
        }
        for (Player player : players) {
            System.out.println(player.getName() + " is choosing a sector to score.");
            player.scoreSector(board);
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

    public void displayBoard() {
        // Print column headers
        System.out.print("   ");
        for (int col = 1; col <= map[0].length; col++) {
            System.out.printf("%-3d", col);
        }
        System.out.println();

        // Print each row with row headers
        for (int row = 0; row < map.length; row++) {
            System.out.printf("%-2s ", (char) ('A' + row)); // Row labels (A, B, C, ...)
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col].isEmpty()) {
                    System.out.print(" . "); // Print empty spaces as "."
                } else {
                    System.out.printf("%-3s", map[row][col]); // Print the board element
                }
            }
            System.out.println();
        }
    }
}
