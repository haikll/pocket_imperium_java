package ProjetLatest2;
import java.util.*;

class Board {
    private static Board instance;
    private List<Sector> sectors;
    private int currentStep;


    public Board() {
        this.sectors = new ArrayList<>();
        this.currentStep = 0;
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
        {0, 0, 0, 0, 0, 0, 0, 0, 1},
        {2, 1, 0, 0, 3, 0, 0, 2, 0},
        {0, 0, 2, 0, 0, 0, 1, 0, 0},
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

    // Getter for current step
    public int getCurrentStep() {
        return currentStep;
    }

    // Setter to update the current step
    public void nextStep() {
        currentStep++;
    }
}