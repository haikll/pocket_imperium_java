package ProjetLatest;

import java.util.*;

class ExploreCommand extends Command {

    @Override
    public void execute(Player player, Board board, Game game) {
        int explorePlayers = (int) game.getPlayers().stream()
                .filter(p -> p.getCommandAt(game.getCurrentStep()) instanceof ExploreCommand)
                .count();

        int fleetsToMove = switch (explorePlayers) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        System.out.println(player.getName() + " can move " + fleetsToMove + " fleets.");

        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < fleetsToMove; i++) {
            // Choose the sector to move ships from
            Sector fromSector = selectControlledSector(player, board, scanner);
            if (fromSector == null) return; // No valid sectors to move from

            // Ask how many ships to move
            int shipsToMove = selectNumberOfShips(fromSector, scanner);

            // Choose the target sector - considering adjacency and Level 3 rules
            Sector toSector = selectTargetSector(fromSector, player, board, scanner);
            if (toSector == null) return; // No valid sectors to move to

            // Move the ships
            for (int j = 0; j < shipsToMove; j++) {
                Ship movedShip = fromSector.getShips().remove(0);
                toSector.getShips().add(movedShip);
            }

            System.out.println("Moved " + shipsToMove + " ships from " + fromSector.getName() + " to " + toSector.getName() + ".");

            // If the target sector was unoccupied, claim it
            if (toSector.getOwner() == null) {
                toSector.setOwner(player);
                System.out.println(player.getName() + " now controls " + toSector.getName() + ".");
            }
        }
    }

    private Sector selectControlledSector(Player player, Board board, Scanner scanner) {
        List<Sector> controlledSectors = board.getSectors().stream()
                .filter(sector -> sector.getOwner() == player && !sector.getShips().isEmpty())
                .toList();

        if (controlledSectors.isEmpty()) {
            System.out.println("No valid sectors to move ships from.");
            return null; // Exit early since no valid moves are possible
        }

        while (true) {
            System.out.println("Choose a sector you control to move ships from:");
            controlledSectors.forEach(sector ->
                    System.out.println(sector.getName() + ": Level " + sector.getLevel() + ", Ships: " + sector.getShips().size()));

            String fromSectorName = scanner.nextLine();
            final String finalFromSectorName = fromSectorName; // Create an effectively final variable
            Sector fromSector = controlledSectors.stream()
                    .filter(sector -> sector.getName().equalsIgnoreCase(finalFromSectorName))
                    .findFirst()
                    .orElse(null);

            if (fromSector != null) return fromSector;
            System.out.println("Invalid sector selection. Please try again.");
        }
    }

    private int selectNumberOfShips(Sector fromSector, Scanner scanner) {
        while (true) {
            System.out.println("How many ships to move?");
            try {
                int shipsToMove = scanner.nextInt();
                scanner.nextLine(); // Clear the newline character
                if (shipsToMove > 0 && shipsToMove <= fromSector.getShips().size()) return shipsToMove;
                System.out.println("Invalid number of ships. Try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private Sector selectTargetSector(Sector fromSector, Player player, Board board, Scanner scanner) {
        while (true) {
            System.out.println("Choose a target sector to move to (adjacent to " + fromSector.getName() + "):");
            List<Sector> possibleTargets = getAdjacentSectors(fromSector, board, player);

            if (possibleTargets.isEmpty()) {
                System.out.println("No valid target sectors available.");
                return null; // Exit early since no valid moves are possible
            }

            possibleTargets.forEach(sector ->
                    System.out.println(sector.getName() + ": Level " + sector.getLevel()));

            String toSectorName = scanner.nextLine();
            final String finalToSectorName = toSectorName; // Create an effectively final variable
            Sector toSector = possibleTargets.stream()
                    .filter(sector -> sector.getName().equalsIgnoreCase(finalToSectorName))
                    .findFirst()
                    .orElse(null);

            if (toSector != null) {
                // If the target is a Level 3 sector, ensure no other player occupies any Level 3 sector
                if (toSector.getLevel() == 3 && isLevel3OccupiedByOtherPlayers(board, player)) {
                    System.out.println("You cannot explore Level 3 sectors because they are occupied by another player.");
                } else {
                    return toSector;
                }
            } else {
                System.out.println("Invalid target sector selection. Please try again.");
            }
        }
    }

    private List<Sector> getAdjacentSectors(Sector fromSector, Board board, Player player) {
        return board.getSectors().stream()
                .filter(sector -> isAdjacent(fromSector, sector)
                        && (sector.getOwner() == null || sector.getOwner() == player)
                        && !isBlockedByOtherPlayer(sector))
                .toList();
    }

    private boolean isBlockedByOtherPlayer(Sector sector) {
        return sector.getOwner() != null; // Sector is occupied by another player
    }

    private boolean isLevel3OccupiedByOtherPlayers(Board board, Player currentPlayer) {
        return board.getSectors().stream()
                .filter(sector -> sector.getLevel() == 3)
                .anyMatch(sector -> sector.getOwner() != null && sector.getOwner() != currentPlayer);
    }

    private boolean isAdjacent(Sector fromSector, Sector toSector) {
        // Assuming adjacency is based on coordinates or names (e.g., "G2" -> "G1", "F3", etc.)
        String fromName = fromSector.getName();
        String toName = toSector.getName();
        char fromRow = fromName.charAt(0);
        char toRow = toName.charAt(0);
        int fromCol = Integer.parseInt(fromName.substring(1));
        int toCol = Integer.parseInt(toName.substring(1));

        return Math.abs(fromRow - toRow) <= 1 && Math.abs(fromCol - toCol) <= 1;
    }

    @Override
    public void execute(Player player, Board board) {
        // Placeholder for single-player mode or specific board actions
    }
}
