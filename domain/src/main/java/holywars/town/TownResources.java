package holywars.town;

import holywars.world.LuxuryResource;
import java.time.Duration;
import java.time.Instant;

public record TownResources(LuxuryResource luxury, long woodTicks, long luxuryTicks, Instant lastUpdate) {

    private static final long TICKS_PER_UNIT = 3600;
    private static final long INITIAL_WOOD = 500;
    private static final long INITIAL_LUXURY = 100;
    private static final long WOOD_PER_HOUR = 30;
    private static final long LUXURY_PER_HOUR = 10;

    public static TownResources initial(LuxuryResource luxury, Instant foundedAt) {
        return new TownResources(luxury, INITIAL_WOOD * TICKS_PER_UNIT, INITIAL_LUXURY * TICKS_PER_UNIT, foundedAt);
    }

    public long wood() {
        return woodTicks / TICKS_PER_UNIT;
    }

    public long luxuryAmount() {
        return luxuryTicks / TICKS_PER_UNIT;
    }

    public TownResources spend(int wood, int luxury) {
        return this;
    }

    public TownResources advancedTo(Instant now) {
        if (now.isBefore(lastUpdate)) {
            throw new InvalidAdvanceInstantException(lastUpdate, now);
        }
        long elapsedSeconds = Duration.between(lastUpdate, now).toSeconds();
        return new TownResources(
                luxury,
                woodTicks + WOOD_PER_HOUR * elapsedSeconds,
                luxuryTicks + LUXURY_PER_HOUR * elapsedSeconds,
                lastUpdate.plusSeconds(elapsedSeconds));
    }
}
