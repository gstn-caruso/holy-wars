package holywars.player;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class PlayerTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void startingPlayerHasFiveHundredGold() {
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);

        assertThat(player.goldAmount()).isEqualTo(500);
    }

    @Test
    void advancedToOneHourLaterRaisesGoldToFiveHundredTwenty() {
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);

        Player advanced = player.advancedTo(NOW.plus(Duration.ofHours(1)));

        assertThat(advanced.goldAmount()).isEqualTo(520);
    }
}
