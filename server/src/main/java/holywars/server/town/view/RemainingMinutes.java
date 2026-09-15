package holywars.server.town.view;

import java.time.Duration;

final class RemainingMinutes {

    private static final long MILLIS_PER_MINUTE = 60_000L;

    private RemainingMinutes() {
    }

    static long roundedUp(Duration remaining) {
        return (remaining.toMillis() + MILLIS_PER_MINUTE - 1) / MILLIS_PER_MINUTE;
    }
}
