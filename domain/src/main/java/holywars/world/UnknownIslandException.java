package holywars.world;

public final class UnknownIslandException extends RuntimeException {

    public UnknownIslandException(IslandId id) {
        super("Unknown island " + id);
    }
}
