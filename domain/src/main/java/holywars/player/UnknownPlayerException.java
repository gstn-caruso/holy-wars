package holywars.player;

public class UnknownPlayerException extends RuntimeException {

    public UnknownPlayerException(PlayerId id) {
        super("No player found with id " + id.value());
    }
}
