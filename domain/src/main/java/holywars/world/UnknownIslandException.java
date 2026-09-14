package holywars.world;

public class UnknownIslandException extends RuntimeException {

    public UnknownIslandException(IslandId id) {
        super("No island found with id " + id.value());
    }
}
