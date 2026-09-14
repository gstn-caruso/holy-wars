package holywars.town;

public final class InvalidBuildingSlotCountException extends RuntimeException {

    public InvalidBuildingSlotCountException(int actualSlotCount) {
        super("A town must have exactly " + Town.REQUIRED_BUILDING_SLOT_COUNT
                + " distinct building slot positions, got " + actualSlotCount);
    }
}
