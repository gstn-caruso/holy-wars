package holywars.world;

import java.util.ArrayList;
import java.util.List;

public record WorldGenerationSettings(int gridWidth, int gridHeight, int islandCount) {

    public WorldGenerationSettings {
        if (islandCount > gridWidth * gridHeight) {
            throw new WorldTooSmallException(gridWidth, gridHeight, islandCount);
        }
    }

    public static WorldGenerationSettings standard() {
        return new WorldGenerationSettings(10, 10, 20);
    }

    public List<Coordinate> allCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>(gridWidth * gridHeight);
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                coordinates.add(new Coordinate(x, y));
            }
        }
        return coordinates;
    }
}
