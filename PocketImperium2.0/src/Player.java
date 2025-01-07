package ProjetLatest2;
import java.util.*;
import ProjetLatest2.Strategy;
import ProjetLatest2.AggressiveStrategy;

class Player {
    private String name;
    private boolean isVirtual;
    private Strategy strategy;
    private List<Ship> ships;
    private int score;
    private List<Command> commands;
    private List<Ship> shipsInPlay;  // Ships currently on the board
    private int supply;

    public Player(String name, boolean isVirtual, Strategy strategy) {
        this.name = name;
        this.isVirtual = isVirtual;
        this.strategy = strategy;
        this.ships = new ArrayList<>();
        this.score = 0;
        this.commands = new ArrayList<>();
        this.shipsInPlay = new ArrayList<>();
        this.supply = 15;
    }

    public Player(String name, boolean isVirtual) {
        this(name, isVirtual, null);
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public boolean isAI() {
        return isVirtual;
    }

    public int getSupply() {
        return supply;
    }

    public void addToSupply(int numberOfShips) {
        this.supply += numberOfShips;
    }

    public boolean hasEnoughShips(int numberOfShips) {
        return supply >= numberOfShips;
    }

    public void useShips(int numberOfShips) {
        if (hasEnoughShips(numberOfShips)) {
            this.supply -= numberOfShips;
        } else {
            System.out.println("Not enough ships in supply.");
        }
    }

    public void addShipToBoard(Ship ship) {
        shipsInPlay.add(ship);
        useShips(1);  // Use one ship from the supply when it's placed on the board
    }

    public void removeShipFromBoard(Ship ship) {
        shipsInPlay.remove(ship);
        addToSupply(1);  // Return one ship to the supply when it's removed from the board
    }

    public List<Ship> getShipsInPlay() {
        return shipsInPlay;
    }

    public void placeShipsOnSector(Player player, Sector sector, int numberOfShips) {
        if (player.hasEnoughShips(numberOfShips)) {
            for (int i = 0; i < numberOfShips; i++) {
                Ship newShip = new Ship();  // Ensure Ship has appropriate constructor or attributes
                sector.getShips().add(newShip);
                player.addShipToBoard(newShip);  // Add ship to board and decrease supply
            }
            System.out.println(numberOfShips + " ships placed on sector " + sector.getName());
        } else {
            System.out.println("Not enough ships in supply to place on sector.");
        }
    }
    

    public void chooseCommandOrder(Game game) {
        commands.clear(); // Clear any previous commands
        Scanner scanner = new Scanner(System.in);
        List<Command> availableCommands = Arrays.asList(new ExpandCommand(), new ExploreCommand(), new ExterminateCommand());

        if (isVirtual && strategy != null) {
            strategy.decideCommandOrder(this, game); // Pass the Player and Game objects
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
        command.execute(this, Board.getInstance());
    }

    // Getter for score
    public int getScore() {
        return score;
    }

    // Method to add points to the player's score
    public void addScore(int points) {
        this.score += points;
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

    public List<Command> getCommands() {
        return commands;
    }

    // Setter for commands (if you need to modify the list directly)
    public void setCommands(List<Command> commands) {
        this.commands = commands;
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