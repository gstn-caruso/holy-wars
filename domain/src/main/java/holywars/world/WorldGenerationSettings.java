package holywars.world;

public record WorldGenerationSettings(GridSize grid, int islandCount) {

    public WorldGenerationSettings {
        if (islandCount > grid.cellCount()) {
            throw new WorldTooSmallException(grid.width(), grid.height(), islandCount);
        }
    }

    public static WorldGenerationSettings standard() {
        return new WorldGenerationSettings(new GridSize(10, 10), 20);
    }
}
