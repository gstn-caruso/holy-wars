package holywars.town;

public record BuildingView(BuildingType type, int level) {

    public static BuildingView standing(BuildingType type, int level) {
        return new BuildingView(type, level);
    }
}
