package holywars.town;

public final class InvalidBuildingSlotPositionException extends RuntimeException {

    public InvalidBuildingSlotPositionException(int position) {
        super("Building slot position must be between " + BuildingSlot.MIN_POSITION + " and "
                + BuildingSlot.MAX_POSITION + ", got " + position);
    }
}
