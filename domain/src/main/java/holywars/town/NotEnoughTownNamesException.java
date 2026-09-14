package holywars.town;

public class NotEnoughTownNamesException extends RuntimeException {

    public NotEnoughTownNamesException(int requested, int poolSize) {
        super("Cannot pick " + requested + " distinct town names from a pool of " + poolSize);
    }
}
