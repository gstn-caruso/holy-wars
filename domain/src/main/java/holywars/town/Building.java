package holywars.town;

public record Building(BuildingType type, int level) {

    public Building {
        if (level < 1) {
            throw new InvalidBuildingLevelException(level);
        }
    }
}
