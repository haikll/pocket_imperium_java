package ProjetLatest2;
import java.util.*;

public class ExterminateCommand extends Command {
    @Override
    public void execute(Player player, Board board, Game game) {
        System.out.println(player.getName() + " is exterminating.");

        int exterminatePlayers = (int) game.getPlayers().stream()
                .filter(p -> p.getCommandAt(game.getCurrentStep()) instanceof ExterminateCommand)
                .count();

        int sectorsToAttack = switch (exterminatePlayers) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        System.out.println(player.getName() + " can attack " + sectorsToAttack + " sectors.");

        // If it's AI's turn, execute the command automatically
        if (player.isAI()) {
            for (int i = 0; i < sectorsToAttack; i++) {
                // AI selects a controlled sector with ships to attack from
                Sector fromSector = selectControlledSector(player, board);
                if (fromSector == null) return; // No valid sectors to move from

                // AI selects a target enemy sector to attack (adjacent sectors only)
                Sector targetSector = selectTargetSector(player, board, fromSector);
                if (targetSector == null) return; // No valid enemy sectors to attack

                // AI selects how many ships to send to attack (either the maximum or a reasonable number)
                int maxShipsToAttack = Math.min(fromSector.getShips().size(), targetSector.getShips().size());
                int attackingShips = maxShipsToAttack; // For simplicity, AI uses the max number

                System.out.println(player.getName() + " attacks " + targetSector.getOwner().getName() + "'s sector " + targetSector.getName() +
                        " with " + attackingShips + " ships.");

                int remainingDefenders = Math.max(0, targetSector.getShips().size() - attackingShips);
                int remainingAttackers = Math.max(0, attackingShips - targetSector.getShips().size());

                // Remove ships from both sides
                targetSector.getShips().clear();
                fromSector.getShips().subList(0, attackingShips).clear();

                // If attackers win (remainingAttackers > 0), they take control
                if (remainingAttackers > 0) {
                    for (int j = 0; j < remainingAttackers; j++) {
                        targetSector.getShips().add(new Ship(player));
                    }
                    targetSector.setOwner(player);
                    System.out.println(player.getName() + " takes control of " + targetSector.getName() + " with " + remainingAttackers + " ships remaining.");
                }
                // If defenders win (remainingDefenders > 0), the sector remains with the defender
                else if (remainingDefenders > 0) {
                    for (int d = 0; d < remainingDefenders; d++) {
                        targetSector.getShips().add(new Ship(targetSector.getOwner()));
                    }
                    System.out.println(targetSector.getOwner().getName() + " retains control of " + targetSector.getName() + " with " + remainingDefenders + " ships remaining.");
                }
                // If there's a draw (both sides lose all ships)
                else {
                    targetSector.setOwner(null); // No one owns the sector
                    System.out.println(targetSector.getName() + " is now neutral as both attackers and defenders lost all their ships.");
                }

                // Check all sectors and remove ownership if no ships remain
                board.getSectors().forEach(sector -> {
                    if (sector.getShips().isEmpty() && sector.getOwner() != null) {
                        sector.setOwner(null);
                    }
                });
            }
        } else {
            Scanner scanner = new Scanner(System.in);

            // Display controlled sectors with ships
            System.out.println("Choose a sector you control to attack from:");
            List<Sector> controlledSectors = board.getSectors().stream()
                    .filter(sector -> sector.getOwner() == player && !sector.getShips().isEmpty())
                    .toList();

            if (controlledSectors.isEmpty()) {
                System.out.println("No sectors with ships available to attack from.");
                return;
            }

            controlledSectors.forEach(sector ->
                System.out.println(sector.getName() + ": Level " + sector.getLevel() +
                        ", Ships: " + sector.getShips().size()));

            String fromSectorName = scanner.nextLine();
            Sector fromSector = controlledSectors.stream()
                    .filter(sector -> sector.getName().equalsIgnoreCase(fromSectorName))
                    .findFirst()
                    .orElse(null);

            if (fromSector != null) {
                Sector targetSector = null;

                // Loop until a valid enemy sector with ships is chosen (adjacent to the player's controlled sectors)
                while (targetSector == null) {
                    System.out.println("Choose an adjacent enemy sector to attack:");
                    List<Sector> adjacentEnemySectors = new ArrayList<>();

                    // Find the adjacent enemy sectors surrounding the player's sector
                    for (Sector controlledSector : controlledSectors) {
                        for (Sector adjacentSector : controlledSector.getAdjacentSectors()) {
                            if (adjacentSector.getOwner() != player && adjacentSector.getOwner() != null && !adjacentSector.getShips().isEmpty()) {
                                adjacentEnemySectors.add(adjacentSector);
                            }
                        }
                    }

                    if (adjacentEnemySectors.isEmpty()) {
                        System.out.println("No valid adjacent enemy sectors with ships available to attack.");
                        return;
                    }

                    adjacentEnemySectors.forEach(sector ->
                        System.out.println(sector.getName() + ": Level " + sector.getLevel() +
                                ", Owner: " + sector.getOwner().getName() +
                                ", Ships: " + sector.getShips().size()));

                    String targetSectorName = scanner.nextLine();
                    targetSector = adjacentEnemySectors.stream()
                            .filter(sector -> sector.getName().equalsIgnoreCase(targetSectorName))
                            .findFirst()
                            .orElse(null);

                    if (targetSector == null) {
                        System.out.println("Invalid target sector. Please try again.");
                    }
                }

                int maxShipsToAttack = Math.min(fromSector.getShips().size(), targetSector.getShips().size());

                int attackingShips = 0;

                // Loop to get valid number of attacking ships
                while (attackingShips <= 0 || attackingShips > maxShipsToAttack) {
                    System.out.println("How many ships to attack with? (Max: " + maxShipsToAttack + ")");
                    if (scanner.hasNextInt()) {
                        attackingShips = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (attackingShips <= 0 || attackingShips > maxShipsToAttack) {
                            System.out.println("Invalid number of ships. Please try again.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.next(); // Clear invalid input
                    }
                }

                System.out.println("Battle outcome: " + attackingShips + " ships attack " + targetSector.getShips().size() + " defenders.");
                int remainingDefenders = Math.max(0, targetSector.getShips().size() - attackingShips);
                int remainingAttackers = Math.max(0, attackingShips - targetSector.getShips().size());

                // Remove ships from both sides
                targetSector.getShips().clear();
                fromSector.getShips().subList(0, attackingShips).clear();

                // If attackers win (remainingAttackers > 0), they take control
                if (remainingAttackers > 0) {
                    for (int i = 0; i < remainingAttackers; i++) {
                        targetSector.getShips().add(new Ship(player));
                    }
                    targetSector.setOwner(player);
                    System.out.println(player.getName() + " takes control of " + targetSector.getName() + " with " + remainingAttackers + " ships remaining.");
                }
                // If defenders win (remainingDefenders > 0), the sector remains with the defender
                else if (remainingDefenders > 0) {
                    for (int i = 0; i < remainingDefenders; i++) {
                        targetSector.getShips().add(new Ship(targetSector.getOwner()));
                    }
                    System.out.println(targetSector.getOwner().getName() + " retains control of " + targetSector.getName() + " with " + remainingDefenders + " ships remaining.");
                }
                // If there's a draw (both sides lose all ships)
                else {
                    targetSector.setOwner(null); // No one owns the sector
                    System.out.println(targetSector.getName() + " is now neutral as both attackers and defenders lost all their ships.");
                }
                // Check all sectors and remove ownership if no ships remain
                board.getSectors().forEach(sector -> {
                    if (sector.getShips().isEmpty() && sector.getOwner() != null) {
                        sector.setOwner(null);
                    }
                });
            }
        }
    }

    // AI-controlled selection of the sector from which to attack
    private Sector selectControlledSector(Player player, Board board) {
        List<Sector> controlledSectors = board.getSectors().stream()
                .filter(sector -> sector.getOwner() == player && !sector.getShips().isEmpty())
                .toList();
        if (controlledSectors.isEmpty()) return null;
        return controlledSectors.get(new Random().nextInt(controlledSectors.size())); // AI selects a random sector to attack from
    }

    // Select an enemy sector surrounding the player's controlled sectors
    private Sector selectTargetSector(Player player, Board board, Sector fromSector) {
        List<Sector> adjacentEnemySectors = new ArrayList<>();

        // Find the adjacent enemy sectors surrounding the player's sector
        for (Sector adjacentSector : fromSector.getAdjacentSectors()) {
            if (adjacentSector.getOwner() != player && adjacentSector.getOwner() != null && !adjacentSector.getShips().isEmpty()) {
                adjacentEnemySectors.add(adjacentSector);
            }
        }

        if (adjacentEnemySectors.isEmpty()) {
            return null;
        }

        // Return a random enemy sector for AI, or prompt the player for input in the non-AI case
        return adjacentEnemySectors.get(new Random().nextInt(adjacentEnemySectors.size()));
    }
}