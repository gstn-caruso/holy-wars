package holywars.town;

public class InvalidBuildingSlotCountException extends RuntimeException {

    public InvalidBuildingSlotCountException(int slotCount) {
        super("A town must have exactly " + BuildingSlot.HIGHEST_POSITION
                + " building slots with distinct positions, had " + slotCount);
    }
}
