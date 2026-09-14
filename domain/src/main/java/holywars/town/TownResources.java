package holywars.town;

import holywars.world.LuxuryResource;
import java.time.Instant;

public record TownResources(LuxuryResource luxury, long woodTicks, long luxuryTicks, Instant lastUpdate) {

    private static final long TICKS_PER_UNIT = 3600;
    private static final long INITIAL_WOOD = 500;
    private static final long INITIAL_LUXURY = 100;

    public static TownResources initial(LuxuryResource luxury, Instant foundedAt) {
        return new TownResources(luxury, INITIAL_WOOD * TICKS_PER_UNIT, INITIAL_LUXURY * TICKS_PER_UNIT, foundedAt);
    }

    public long wood() {
        return woodTicks / TICKS_PER_UNIT;
    }

    public long luxuryAmount() {
        return luxuryTicks / TICKS_PER_UNIT;
    }
}
