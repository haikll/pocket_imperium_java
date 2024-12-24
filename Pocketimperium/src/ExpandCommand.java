package ProjetLatest;
import java.util.*;

class ExpandCommand extends Command {
    public void execute(Player player, Board board, Game game) {
        int expandPlayers = (int) game.getPlayers().stream()
            .filter(p -> p.getCommandAt(game.getCurrentStep()) instanceof ExpandCommand)
            .count();

        int shipsToPlace = switch (expandPlayers) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        System.out.println(player.getName() + " can place " + shipsToPlace + " ships.");

        List<Sector> controlledSectors = board.getSectors().stream()
            .filter(sector -> sector.getOwner() == player) // Filter sectors owned by the player
            .toList();

        if (controlledSectors.isEmpty()) {
            System.out.println("No valid sectors to place ships in.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < shipsToPlace; i++) {
            System.out.println("Choose a sector you control to place ships in:");
            controlledSectors.forEach(sector ->
                System.out.println(sector.getName() + ": Level " + sector.getLevel() + ", Ships: " + sector.getShips().size()));

            String targetSectorName = scanner.nextLine();
            Sector targetSector = controlledSectors.stream()
                .filter(sector -> sector.getName().equalsIgnoreCase(targetSectorName))
                .findFirst()
                .orElse(null);

            if (targetSector == null) {
                System.out.println("Invalid sector selection.");
                continue;
            }

            targetSector.getShips().add(new Ship(player));
            System.out.println("Placed 1 ship in " + targetSector.getName() + ".");
        }
    }
}




