package holywars.town;

public class MismatchedBuildingTypeException extends RuntimeException {

    public MismatchedBuildingTypeException(SlotKind kind, BuildingType type) {
        super("Building type " + type + " does not belong to slot kind " + kind);
    }
}
