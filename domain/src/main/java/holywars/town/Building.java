package holywars.town;

public final class Building {

    private final BuildingType type;
    private final int level;

    private Building(BuildingType type, int level) {
        this.type = type;
        this.level = level;
    }

    public static Building standing(BuildingType type, int level) {
        return new Building(type, level);
    }

    public BuildingType type() {
        return type;
    }

    public int level() {
        return level;
    }

    public BuildingView view() {
        return BuildingView.standing(type, level);
    }
}
