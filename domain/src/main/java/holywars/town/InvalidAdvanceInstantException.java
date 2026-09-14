package holywars.town;

import java.time.Instant;

public class InvalidAdvanceInstantException extends RuntimeException {

    public InvalidAdvanceInstantException(Instant lastUpdate, Instant requested) {
        super("Cannot advance to " + requested + ", which is before the last update at " + lastUpdate);
    }
}
