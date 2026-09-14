package holywars.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class PlayerTest {

    private final Instant startedAt = Instant.parse("2024-01-01T00:00:00Z");

    @Test
    void humanFactoryBuildsAPlayerOfKindHuman() {
        Player player = Player.human(new PlayerId(1), "Jugador", 500, startedAt);

        assertThat(player.id()).isEqualTo(new PlayerId(1));
        assertThat(player.name()).isEqualTo("Jugador");
        assertThat(player.kind()).isEqualTo(PlayerKind.HUMAN);
        assertThat(player.gold()).isEqualTo(500);
    }

    @Test
    void aiFactoryBuildsAPlayerOfKindAi() {
        Player player = Player.ai(new PlayerId(2), "Perseo", 500, startedAt);

        assertThat(player.id()).isEqualTo(new PlayerId(2));
        assertThat(player.name()).isEqualTo("Perseo");
        assertThat(player.kind()).isEqualTo(PlayerKind.AI);
        assertThat(player.gold()).isEqualTo(500);
    }

    @Test
    void aPlayersGoldGrowsTwentyPerHour() {
        Player player = Player.human(new PlayerId(1), "Jugador", 500, startedAt);

        Player afterOneHour = player.advancedTo(startedAt.plus(Duration.ofHours(1)));
        Player afterThirtyMinutes = player.advancedTo(startedAt.plus(Duration.ofMinutes(30)));

        assertThat(afterOneHour.gold()).isEqualTo(520);
        assertThat(afterThirtyMinutes.gold()).isEqualTo(510);
    }

    @Test
    void aPlayerRejectsAdvancingIntoThePast() {
        Player player = Player.human(new PlayerId(1), "Jugador", 500, startedAt);

        assertThatThrownBy(() -> player.advancedTo(startedAt.minusSeconds(1)))
                .isInstanceOf(InvalidAdvanceInstantException.class);
    }
}
