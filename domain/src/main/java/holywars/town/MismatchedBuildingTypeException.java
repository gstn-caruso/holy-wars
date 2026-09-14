package holywars.town;

public final class MismatchedBuildingTypeException extends RuntimeException {

    public MismatchedBuildingTypeException(BuildingSlotKind slotKind, BuildingSlotKind builtKind) {
        super("Cannot build a " + builtKind + " in a " + slotKind + " slot");
    }

    public MismatchedBuildingTypeException(BuildingSlotKind slotKind, BuildingType type) {
        super("Cannot build a " + type + " in a " + slotKind + " slot");
    }
}
