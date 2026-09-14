package holywars.player;

import holywars.resources.Elapsed;
import holywars.resources.ResourceStock;
import java.time.Duration;
import java.time.Instant;

public record Player(PlayerId id, String name, ResourceStock gold, Instant lastUpdate) {

    private static final int STARTING_GOLD = 500;
    private static final int GOLD_PER_HOUR = 20;

    public static Player starting(PlayerId id, String name, Instant now) {
        return new Player(id, name, ResourceStock.of(STARTING_GOLD, GOLD_PER_HOUR), now);
    }

    public static Player reconstituted(PlayerId id, String name, long goldTicks, Instant lastUpdate) {
        return new Player(id, name, new ResourceStock(goldTicks, GOLD_PER_HOUR), lastUpdate);
    }

    public Player advancedTo(Instant now) {
        Duration elapsed = Elapsed.since(lastUpdate, now);
        return new Player(id, name, gold.advancedTo(elapsed), lastUpdate.plus(elapsed));
    }

    public int goldAmount() {
        return gold.amount();
    }
}
