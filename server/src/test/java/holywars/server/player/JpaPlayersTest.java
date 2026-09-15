package holywars.server.player;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class JpaPlayersTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private JpaPlayerTable jpaPlayerTable;

    private JpaPlayers players;

    @BeforeEach
    void setUp() {
        players = new JpaPlayers(jpaPlayerTable);
    }

    @Test
    void savesAPlayerAndFindsItBackEqual() {
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);

        players.save(player);

        assertThat(players.find()).contains(player);
    }

    @Test
    void savesAnAdvancedPlayerAndKeepsItsGoldTicksAndGoldUpdatedAt() {
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW).advancedTo(NOW.plus(Duration.ofHours(1)));

        players.save(player);

        assertThat(players.find()).contains(player);
    }
}
