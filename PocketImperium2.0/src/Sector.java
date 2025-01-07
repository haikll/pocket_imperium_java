package ProjetLatest2;
import java.util.*;

class Sector {
    private int level;
    private String name; // Name of the sector
    private Player owner;
    private List<Ship> ships;
    private List<Sector> adjacentSectors;
    private List<Player> players;

    public Sector(int level, String name) {
        this.level = level;
        this.name = name;
        this.level = level;
        this.ships = new ArrayList<>();
        this.adjacentSectors = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name; // This allows other classes to access the sector name
    }
   
    
    public Player getOwner() {
        return owner;
    }
    
    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
        if (owner != null) {
            System.out.println(owner.getName() + " now controls " + this.name + ".");
        }
    }

    public List<Sector> getAdjacentSectors() {
        return adjacentSectors;
    }

    public void sustainShips(Player player) {
        // Calculate the maximum number of ships this sector can sustain
        int maxShips = 1 + this.getLevel();  // 1 ship + level of the system in the sector
    
        // Calculate the excess number of ships that need to be removed
        int excessShips = getShips().size() - maxShips;
    
        
        if (excessShips > 0) {
            // Remove the excess ships from the sector
            for (int i = 0; i < excessShips; i++) {
                Ship removedShip = getShips().remove(0); // Remove ship from the sector
                player.addToSupply(1); // Return the removed ship to the player's supply
            }
            System.out.println("Removed " + excessShips + " excess ships from " + getName() + " due to resource limitations.");
        } 

        for (Player playerInLoop : players) {
            System.out.println("Total ships in supply for " + player.getName() + ": " + player.getSupply() + ".");
        }
    }

    
    public static void scoreSectors(Board board, List<Player> players, Player startPlayer) {
        List<Sector> scoredSectors = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
    
        // Iterate over players starting from the start player
        int startIndex = players.indexOf(startPlayer);
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get((startIndex + i) % players.size());
    
            // Get available sectors for the player (sectors they own, excluding already scored ones)
            List<Sector> availableSectors = board.getSectors().stream()
                .filter(sector -> sector.getOwner() != null && sector.getOwner().equals(currentPlayer)) // Sector must be owned by the player
                .filter(sector -> !scoredSectors.contains(sector)) // Sector not already scored
                .toList();
    
            if (availableSectors.isEmpty()) {
                System.out.println(currentPlayer.getName() + " has no eligible sectors to score. Skipping.");
                continue;
            }
    
            // If the player owns the Tri-Prime sector, remove it from the scoring options
            Sector triPrimeSector = board.getSectors().stream()
                .filter(sector -> sector.getName().equalsIgnoreCase("Tri-Prime") && sector.getOwner().equals(currentPlayer))
                .findFirst()
                .orElse(null);
    
            if (triPrimeSector != null) {
                System.out.println(currentPlayer.getName() + " owns the Tri-Prime sector, but cannot score it directly. Choose another sector.");
                availableSectors.remove(triPrimeSector); // Remove Tri-Prime from available sectors
            }
    
            // AI logic: if the current player is an AI, they choose their own sector automatically
            if (currentPlayer.isAI()) {
                System.out.println(currentPlayer.getName() + " (AI) is choosing a sector...");
                // AI selects a random sector from available sectors
                Sector chosenSector = availableSectors.get((int) (Math.random() * availableSectors.size()));
                currentPlayer.addScore(chosenSector.getLevel());
                scoredSectors.add(chosenSector);
                System.out.println(currentPlayer.getName() + " (AI) scored " + chosenSector.getLevel() + " points from " + chosenSector.getName() + "!");
            } else {
                // Display eligible sectors for scoring
                System.out.println(currentPlayer.getName() + ", choose a sector to score:");
                for (int j = 0; j < availableSectors.size(); j++) {
                    Sector sector = availableSectors.get(j);
                    System.out.println((j + 1) + ". " + sector.getName() + " (Level " + sector.getLevel() + ")");
                }
    
                int choice = -1;
                boolean validChoice = false;
    
                // Loop until a valid choice is made
                while (!validChoice) {
                    System.out.print("Enter the number of the sector: ");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        if (choice > 0 && choice <= availableSectors.size()) {
                            validChoice = true; // valid choice
                        } else {
                            System.out.println("Invalid sector number, please try again.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.next(); // Clear the invalid input
                    }
                }
    
                // Get the chosen sector and score it
                Sector chosenSector = availableSectors.get(choice - 1);
                currentPlayer.addScore(chosenSector.getLevel());
                scoredSectors.add(chosenSector);
                System.out.println(currentPlayer.getName() + " scored " + chosenSector.getLevel() + " points from " + chosenSector.getName() + "!");
            }
        }
    
        // End of scoring round
        System.out.println("Scoring round complete.");
    }
    
    
    

    
}