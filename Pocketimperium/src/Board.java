package ProjetLatest;
import java.util.*;

class Board {
    private static Board instance;
    private List<Sector> sectors;


    public Board() {
        this.sectors = new ArrayList<>();
    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    public void setup() {
    int[][] sectorLevels = {
    	{2, 0, 0, 1, 0, 1, 0, 0, 0},
        {0, 0, 1, 0, 2, 0, 2, 1, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 2},
        {0, 0, 0, 3, 3, 3, 0, 0, 1},
        {2, 1, 0, 3, 3, 3, 0, 2, 0},
        {0, 0, 2, 3, 3, 3, 1, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 2, 0},
        {1, 0, 0, 1, 0, 1, 0, 0, 0},
        {0, 0, 2, 0, 2, 0, 1, 0, 1}
    };

    for (int row = 0; row < sectorLevels.length; row++) {
        for (int col = 0; col < sectorLevels[row].length; col++) {
            String sectorName = Character.toString('A' + row) + (col + 1); // Generate names like A1, A2, ...
            sectors.add(new Sector(sectorLevels[row][col], sectorName));
        }
    }
                }


    public List<Sector> getSectors() {
        return sectors;
    }
}
