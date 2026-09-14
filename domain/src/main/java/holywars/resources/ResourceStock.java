package holywars.resources;

import java.time.Duration;
import java.util.Optional;

public record ResourceStock(long ticks, int ratePerHour) {

    private static final long TICKS_PER_UNIT = 3600;
    private static final long MILLIS_PER_HOUR = 3_600_000L;

    public static ResourceStock of(int units, int ratePerHour) {
        return new ResourceStock(units * TICKS_PER_UNIT, ratePerHour);
    }

    public int amount() {
        return (int) (ticks / TICKS_PER_UNIT);
    }

    public ResourceStock advancedTo(Duration elapsed) {
        long advancedTicks = ticks + ratePerHour * TICKS_PER_UNIT * elapsed.toMillis() / MILLIS_PER_HOUR;
        return new ResourceStock(advancedTicks, ratePerHour);
    }

    public Optional<ResourceStock> spend(int units) {
        if (amount() < units) {
            return Optional.empty();
        }
        return Optional.of(new ResourceStock(ticks - units * TICKS_PER_UNIT, ratePerHour));
    }
}
