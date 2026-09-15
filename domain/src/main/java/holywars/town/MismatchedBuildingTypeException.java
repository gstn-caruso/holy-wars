package holywars.town;

public final class MismatchedBuildingTypeException extends RuntimeException {

    public MismatchedBuildingTypeException(TownPlotKind plotKind, BuildingType type) {
        super("Cannot build a " + type + " in a " + plotKind + " plot");
    }
}
