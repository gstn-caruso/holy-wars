package holywars.town;

public class SlotNotFreeException extends RuntimeException {

    public SlotNotFreeException(int position, SlotState state) {
        super("Building slot " + position + " is not free to start a construction, current state is " + state);
    }
}
