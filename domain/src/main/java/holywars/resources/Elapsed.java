package holywars.resources;

import java.time.Duration;
import java.time.Instant;

public final class Elapsed {

    private Elapsed() {
    }

    public static Duration since(Instant previous, Instant now) {
        if (now.isBefore(previous)) {
            throw new InvalidAdvanceInstantException(previous, now);
        }
        return Duration.ofSeconds(Duration.between(previous, now).getSeconds());
    }
}
