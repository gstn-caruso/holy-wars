package holywars.town;

import java.time.Duration;
import java.time.Instant;

public record Construction(BuildingType type, Instant startedAt, Instant finishesAt) {

    public static Construction startingAt(BuildingType type, Instant startedAt) {
        return new Construction(type, startedAt, startedAt.plus(type.buildTime()));
    }

    public boolean isFinishedBy(Instant now) {
        return !now.isBefore(finishesAt);
    }

    public Duration remaining(Instant now) {
        return Duration.between(now, finishesAt);
    }
}
