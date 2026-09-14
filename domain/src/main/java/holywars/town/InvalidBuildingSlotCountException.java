package holywars.town;

public class InvalidBuildingSlotCountException extends RuntimeException {

    public InvalidBuildingSlotCountException(int slotCount, int distinctPositionCount) {
        super("A town must have exactly " + BuildingSlot.HIGHEST_POSITION
                + " building slots with distinct positions, had " + slotCount
                + " slots with " + distinctPositionCount + " distinct positions");
    }
}
