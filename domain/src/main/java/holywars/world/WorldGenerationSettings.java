package holywars.world;

public record WorldGenerationSettings(int gridWidth, int gridHeight, int islandCount) {

    public WorldGenerationSettings {
        if (islandCount > gridWidth * gridHeight) {
            throw new WorldTooSmallException(gridWidth, gridHeight, islandCount);
        }
    }

    public static WorldGenerationSettings standard() {
        return new WorldGenerationSettings(10, 10, 20);
    }
}
