package ProjetLatest;
import java.util.*;

class Player {
    private String name;
    private boolean isVirtual;
    private Strategy strategy;
    private List<Ship> ships;
    private int score;
    private List<Command> commands;

    public Player(String name, boolean isVirtual, Strategy strategy) {
        this.name = name;
        this.isVirtual = isVirtual;
        this.strategy = strategy;
        this.ships = new ArrayList<>();
        this.score = 0;
        this.commands = new ArrayList<>();
    }

    public Player(String name, boolean isVirtual) {
        this(name, isVirtual, null);
    }

    public void chooseCommandOrder() {
        commands.clear(); // Clear any previous commands
        Scanner scanner = new Scanner(System.in);
        List<Command> availableCommands = Arrays.asList(new ExpandCommand(), new ExploreCommand(), new ExterminateCommand());

        if (isVirtual && strategy != null) {
            strategy.decideCommandOrder();
        } else {
            System.out.println(name + ", choose the order of your commands (1 for Expand, 2 for Explore, 3 for Exterminate):");
            while (commands.size() < 3) {
                try {
                    System.out.println("Enter command " + (commands.size() + 1) + ": ");
                    int commandIndex = scanner.nextInt();
                    if (commandIndex < 1 || commandIndex > 3) {
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                        continue;
                    }
                    commands.add(availableCommands.get(commandIndex - 1));
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Clear invalid input
                }
            }
        }
    }


    public void executeCommand(Command command, Game game) {
        command.execute(this, Board.getInstance(), game);
    }


    public int calculateScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public Command getCommandAt(int index) {
        if (index >= 0 && index < commands.size()) {
            return commands.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid command index: " + index);
        }
    }

    public void scoreSector(Board board) {
        Scanner scanner = new Scanner(System.in);
        List<Sector> sectors = board.getSectors();

        System.out.println("Available sectors:");
        for (Sector sector : sectors) {
            String ownerName = (sector.getOwner() != null) ? sector.getOwner().getName() : "Unoccupied";
            System.out.println(sector.getName() + ": Level " + sector.getLevel() + ", Owner: " + ownerName);
        }

        System.out.println("Choose a sector to score (enter the name, e.g., A1):");
        String sectorName = scanner.nextLine();

        Sector chosenSector = null;
        for (Sector sector : sectors) {
            if (sector.getName().equalsIgnoreCase(sectorName)) {
                chosenSector = sector;
                break;
            }
        }

        if (chosenSector == null) {
            System.out.println("Invalid sector name. Please try again.");
            return;
        }

        if (chosenSector.getOwner() != null && chosenSector.getOwner().equals(this)) {
            score += chosenSector.getLevel();
            System.out.println(name + " scored " + chosenSector.getLevel() + " points from " + chosenSector.getName() + "!");
        } else {
            System.out.println("Invalid sector selection or you don't own it.");
        }
    }





    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
