package holywars.resources;

import holywars.world.LuxuryResource;
import java.time.Duration;
import java.time.Instant;

public record TownResources(ResourceStock wood, ResourceStock luxury, LuxuryResource luxuryResource,
        Instant lastUpdate) {

    private static final int STARTING_WOOD = 500;
    private static final int WOOD_PER_HOUR = 30;
    private static final int STARTING_LUXURY = 100;
    private static final int LUXURY_PER_HOUR = 10;

    public static TownResources starting(LuxuryResource luxuryResource, Instant foundedAt) {
        return new TownResources(
                ResourceStock.of(STARTING_WOOD, WOOD_PER_HOUR),
                ResourceStock.of(STARTING_LUXURY, LUXURY_PER_HOUR),
                luxuryResource,
                foundedAt);
    }

    public TownResources advancedTo(Instant now) {
        Duration elapsed = Elapsed.since(lastUpdate, now);
        return new TownResources(wood.advancedTo(elapsed), luxury.advancedTo(elapsed), luxuryResource, now);
    }

    public int woodAmount() {
        return wood.amount();
    }

    public int luxuryAmount() {
        return luxury.amount();
    }
}
