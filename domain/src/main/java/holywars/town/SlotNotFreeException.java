package holywars.town;

public final class SlotNotFreeException extends RuntimeException {

    public SlotNotFreeException(int position, BuildingSlotState state) {
        super("Building slot " + position + " is not free, it is " + state);
    }
}
