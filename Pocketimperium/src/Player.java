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
            for (int i = 0; i < 3; i++) {
                System.out.println("Enter command " + (i + 1) + ": ");
                int commandIndex = scanner.nextInt();
                commands.add(availableCommands.get(commandIndex - 1));
            }
        }
    }

    public void executeCommand(Command command) {
        command.execute(this, Board.getInstance());
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
        System.out.println("Available sectors:");
        List<Sector> sectors = board.getSectors();
        for (int i = 0; i < sectors.size(); i++) {
            Sector sector = sectors.get(i);
            String ownerName = (sector.getOwner() != null) ? sector.getOwner().getName() : "Unoccupied";
            System.out.println(i + ": Level " + sector.getLevel() + ", Owner: " + ownerName);
        }

        System.out.println("Choose a sector to score (enter index):");
        int sectorIndex = scanner.nextInt();

        if (sectorIndex < 0 || sectorIndex >= sectors.size()) {
            System.out.println("Invalid sector index. Please try again.");
            return;
        }

        Sector chosenSector = sectors.get(sectorIndex);
        if (chosenSector.getOwner() != null && chosenSector.getOwner().equals(this)) {
            score += chosenSector.getLevel(); // Include Level 0 sectors
            System.out.println(name + " scored " + chosenSector.getLevel() + " points!");
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
