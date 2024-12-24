package ProjetLatest;
import java.util.*;

class Sector {
    private int level;
    private String name; // Name of the sector
    private Player owner;
    private List<Ship> ships;

    public Sector(int level, String name) {
        this.level = level;
        this.name = name;
        this.level = level;
        this.ships = new ArrayList<>();
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

    public void setOwner(Player owner) {
        this.owner = owner;
        if (owner != null) {
            System.out.println(owner.getName() + " now controls " + this.name + ".");
        }
    }


    public void sustainShips() {
        // Logic for sustaining ships
    }

    public void scoreSector() {
        System.out.println("Sector " + name + " scored.");
        // Add additional logic for scoring if needed
    }
}
