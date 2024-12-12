package ProjetLatest;
import java.util.*;

class Sector {
    private int level;
    private Player owner;
    private List<Ship> ships;

    public Sector(int level) {
        this.level = level;
        this.ships = new ArrayList<>();
    }

    public int getLevel() {
        return level;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void sustainShips() {
        // Logic for sustaining ships
    }

    public void scoreSector() {
        // Scoring logic
    }


}