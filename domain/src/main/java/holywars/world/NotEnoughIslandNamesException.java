package holywars.world;

public class NotEnoughIslandNamesException extends RuntimeException {

    public NotEnoughIslandNamesException(int requested, int poolSize) {
        super("Cannot pick " + requested + " distinct island names from a pool of " + poolSize);
    }
}
