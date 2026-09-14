package holywars.town;

public class ConflictingSlotContentsException extends RuntimeException {

    public ConflictingSlotContentsException(int position) {
        super("Building slot " + position + " cannot hold a building and a construction at the same time");
    }
}
