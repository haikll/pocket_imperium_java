package ProjetLatest;
import java.util.*;

class ExploreCommand extends Command {
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
        	System.out.println("Choose a sector you control to move ships from:");
        	List<Sector> controlledSectors = board.getSectors().stream()
        	    .filter(sector -> sector.getOwner() == player && !sector.getShips().isEmpty())
        	    .toList();

            if (controlledSectors.isEmpty()) {
                System.out.println("No valid sectors to move ships from.");
                break;
            }

            controlledSectors.forEach(sector ->
                System.out.println(sector.getName() + ": Level " + sector.getLevel() + ", Ships: " + sector.getShips().size()));

            String fromSectorName = scanner.nextLine();
            Sector fromSector = controlledSectors.stream()
                .filter(sector -> sector.getName().equalsIgnoreCase(fromSectorName))
                .findFirst()
                .orElse(null);

            if (fromSector == null) {
                System.out.println("Invalid sector selection.");
                continue;
            }

            System.out.println("How many ships to move?");
            int shipsToMove = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character

            if (shipsToMove > fromSector.getShips().size()) {
                System.out.println("Not enough ships in the sector. Try again.");
                continue;
            }

            System.out.println("Choose a target sector to move to (up to 2 sectors away):");
            List<Sector> possibleTargets = board.getSectors().stream()
                .filter(sector -> sector.getOwner() != null || sector.getOwner() == player)
                .filter(sector -> sector != fromSector)
                .toList();

            possibleTargets.forEach(sector ->
                System.out.println(sector.getName() + ": Level " + sector.getLevel()));

            String toSectorName = scanner.nextLine();
            Sector toSector = possibleTargets.stream()
                .filter(sector -> sector.getName().equalsIgnoreCase(toSectorName))
                .findFirst()
                .orElse(null);

            if (toSector == null) {
                System.out.println("Invalid target sector selection.");
                continue;
            }

            for (int j = 0; j < shipsToMove; j++) {
                Ship movedShip = fromSector.getShips().remove(0);
                toSector.getShips().add(movedShip);
            }

            System.out.println("Moved " + shipsToMove + " ships from " + fromSector.getName() + " to " + toSector.getName() + ".");

            if (toSector.getOwner() == null) {
                toSector.setOwner(player);
                System.out.println(player.getName() + " now controls " + toSector.getName() + ".");
            }
        }
    }
}
