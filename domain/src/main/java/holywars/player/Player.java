package holywars.player;

import holywars.resources.Elapsed;
import holywars.resources.ResourceStock;
import java.time.Instant;

public record Player(PlayerId id, String name, ResourceStock gold, Instant lastUpdate) {

    private static final int STARTING_GOLD = 500;
    private static final int GOLD_PER_HOUR = 20;

    public static Player starting(PlayerId id, String name, Instant now) {
        return new Player(id, name, ResourceStock.of(STARTING_GOLD, GOLD_PER_HOUR), now);
    }

    public Player advancedTo(Instant now) {
        return new Player(id, name, gold.advancedTo(Elapsed.since(lastUpdate, now)), now);
    }

    public int goldAmount() {
        return gold.amount();
    }
}
