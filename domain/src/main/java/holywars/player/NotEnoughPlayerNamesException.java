package holywars.player;

public class NotEnoughPlayerNamesException extends RuntimeException {

    public NotEnoughPlayerNamesException(int requested, int poolSize) {
        super("Cannot pick " + requested + " distinct player names from a pool of " + poolSize);
    }
}
