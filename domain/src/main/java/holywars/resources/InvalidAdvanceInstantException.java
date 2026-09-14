package holywars.resources;

import java.time.Instant;

public final class InvalidAdvanceInstantException extends RuntimeException {

    public InvalidAdvanceInstantException(Instant previous, Instant now) {
        super("Cannot advance time from " + previous + " back to " + now);
    }
}
