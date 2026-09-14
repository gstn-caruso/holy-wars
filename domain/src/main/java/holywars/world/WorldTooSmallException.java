package holywars.world;

public class WorldTooSmallException extends RuntimeException {

    public WorldTooSmallException(int gridWidth, int gridHeight, int islandCount) {
        super("Grid of " + gridWidth + "x" + gridHeight + " cannot hold " + islandCount + " islands");
    }
}
