package holywars.town;

public class UnknownTownException extends RuntimeException {

    public UnknownTownException(TownId id) {
        super("Unknown town: " + id);
    }
}
