package holywars.town;

import java.time.Instant;

public record Construction(BuildingType type, Instant startedAt, Instant finishesAt) {

    public static Construction of(BuildingType type, Instant startedAt) {
        return new Construction(type, startedAt, startedAt.plus(type.buildTime()));
    }
}
