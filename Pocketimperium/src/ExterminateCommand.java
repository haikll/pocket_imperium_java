package ProjetLatest;
import java.util.*;

class ExterminateCommand extends Command {
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

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a sector you control to attack from:");
        List<Sector> controlledSectors = board.getSectors().stream()
            .filter(sector -> sector.getOwner() == player)
            .toList();

        controlledSectors.forEach(sector -> System.out.println(sector.getName() + ": Level " + sector.getLevel()));

        String fromSectorName = scanner.nextLine();
        Sector fromSector = controlledSectors.stream()
            .filter(sector -> sector.getName().equalsIgnoreCase(fromSectorName))
            .findFirst()
            .orElse(null);

        if (fromSector != null && !fromSector.getShips().isEmpty()) {
            System.out.println("Choose an enemy sector to attack:");
            List<Sector> enemySectors = board.getSectors().stream()
                .filter(sector -> sector.getOwner() != null && sector.getOwner() != player)
                .toList();

            enemySectors.forEach(sector -> System.out.println(sector.getName() + ": Level " + sector.getLevel()));
            String targetSectorName = scanner.nextLine();
            Sector targetSector = enemySectors.stream()
                .filter(sector -> sector.getName().equalsIgnoreCase(targetSectorName))
                .findFirst()
                .orElse(null);

            if (targetSector != null) {
                System.out.println("How many ships to attack with?");
                int attackingShips = Math.min(scanner.nextInt(), fromSector.getShips().size());

                System.out.println("Battle outcome: " + attackingShips + " ships attack " + targetSector.getShips().size() + " defenders.");
                int remainingDefenders = Math.max(0, targetSector.getShips().size() - attackingShips);
                int remainingAttackers = Math.max(0, attackingShips - targetSector.getShips().size());

                targetSector.getShips().clear();
                fromSector.getShips().subList(0, attackingShips).clear();

                for (int i = 0; i < remainingAttackers; i++) {
                    targetSector.getShips().add(new Ship(player));
                }

                if (remainingAttackers > 0) {
                    targetSector.setOwner(player);
                }

                System.out.println("After battle, " + remainingAttackers + " ships remain in " + targetSector.getName());
            } else {
                System.out.println("Invalid target sector.");
            }
        } else {
            System.out.println("Invalid starting sector or no ships to attack.");
        }
    }
}
