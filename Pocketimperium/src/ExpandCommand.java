package ProjetLatest;

import java.util.*;

class ExpandCommand extends Command {

    @Override
    public void execute(Player player, Board board, Game game) {
        int expandPlayers = (int) game.getPlayers().stream()
                .filter(p -> p.getCommandAt(game.getCurrentStep()) instanceof ExpandCommand)
                .count();

        // Determine the number of ships to place based on the number of players
        int shipsToPlace = switch (expandPlayers) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        // Display the message about the number of ships the player can place
        printSeparator();
        System.out.println(player.getName() + " can place " + shipsToPlace + " ships.");
        printSeparator();

        // Get the list of sectors controlled by the player
        List<Sector> controlledSectors = board.getSectors().stream()
                .filter(sector -> sector.getOwner() == player && !sector.getShips().isEmpty()) // Check ownership and presence of ships
                .toList();

        if (controlledSectors.isEmpty()) {
            System.out.println("No valid sectors to place ships in.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // Allow the player to place ships
        for (int i = 0; i < shipsToPlace; i++) {
            System.out.println("Choose a sector you control to place a ship:");
            controlledSectors.forEach(sector ->
                    System.out.println(sector.getName() + ": Level " + sector.getLevel() + ", Ships: " + sector.getShips().size())
            );

            String targetSectorName = scanner.nextLine();

            // Find the sector by name
            Sector targetSector = controlledSectors.stream()
                    .filter(sector -> sector.getName().equalsIgnoreCase(targetSectorName))
                    .findFirst()
                    .orElse(null);

            if (targetSector == null) {
                System.out.println("Invalid sector selection. Try again.");
                i--; // Allow the player to retry
                continue;
            }

            // Add a new ship to the selected sector
            targetSector.getShips().add(new Ship(player));
            System.out.println("Placed 1 ship in " + targetSector.getName() + ".");
        }
        printSeparator();
    }

    @Override
    public void execute(Player player, Board board) {
        // Placeholder for single-player mode or specific board actions
    }

    // Utility method to print a separator
    private void printSeparator() {
        System.out.println("--------");
    }
}
