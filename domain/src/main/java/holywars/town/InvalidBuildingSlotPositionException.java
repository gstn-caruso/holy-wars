package holywars.town;

public class InvalidBuildingSlotPositionException extends RuntimeException {

    public InvalidBuildingSlotPositionException(int position) {
        super("Building slot position must be between 1 and " + BuildingSlot.HIGHEST_POSITION + ", was " + position);
    }
}
