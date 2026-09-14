package holywars.player;

import java.time.Duration;
import java.time.Instant;

public record Player(PlayerId id, String name, PlayerKind kind, long goldTicks, Instant lastUpdate) {

    private static final long TICKS_PER_UNIT = 3600;
    private static final long GOLD_PER_HOUR = 20;

    public static Player human(PlayerId id, String name, int startingGold, Instant startedAt) {
        return new Player(id, name, PlayerKind.HUMAN, startingGold * TICKS_PER_UNIT, startedAt);
    }

    public static Player ai(PlayerId id, String name, int startingGold, Instant startedAt) {
        return new Player(id, name, PlayerKind.AI, startingGold * TICKS_PER_UNIT, startedAt);
    }

    public long gold() {
        return goldTicks / TICKS_PER_UNIT;
    }

    public Player advancedTo(Instant now) {
        if (now.isBefore(lastUpdate)) {
            throw new InvalidAdvanceInstantException(lastUpdate, now);
        }
        long elapsedSeconds = Duration.between(lastUpdate, now).toSeconds();
        return new Player(id, name, kind, goldTicks + GOLD_PER_HOUR * elapsedSeconds, now);
    }
}
