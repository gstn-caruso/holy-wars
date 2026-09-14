package holywars.town;

public final class ConflictingSlotContentsException extends RuntimeException {

    public ConflictingSlotContentsException(int position) {
        super("Building slot " + position + " cannot have both a building and a construction");
    }
}
