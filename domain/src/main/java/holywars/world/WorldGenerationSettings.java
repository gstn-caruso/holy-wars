package holywars.world;

public record WorldGenerationSettings(int gridWidth, int gridHeight, int islandCount) {

    public static WorldGenerationSettings standard() {
        return new WorldGenerationSettings(10, 10, 20);
    }
}
