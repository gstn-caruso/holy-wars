package holywars.town;

public final class MismatchedBuildingTypeException extends RuntimeException {

    public MismatchedBuildingTypeException(BuildingSlotKind slotKind, BuildingSlotKind builtKind) {
        super("Cannot build a " + builtKind + " in a " + slotKind + " slot");
    }
}
