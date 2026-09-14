package holywars.game;

public class NotEnoughIslandsForPlayersException extends RuntimeException {

    public NotEnoughIslandsForPlayersException(int players, int islands) {
        super("Cannot place " + players + " players on " + islands + " islands");
    }
}
