package holywars.server.game.service;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.Players;
import holywars.town.Town;
import holywars.town.Towns;
import holywars.world.Island;
import holywars.world.World;
import holywars.world.Worlds;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class NewGameServiceTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private NewGameService newGameService;

    @Autowired
    private Worlds worlds;

    @Autowired
    private Players players;

    @Autowired
    private Towns towns;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void emptyTheSharedDatabase() {
        jdbcTemplate.update("delete from town_plot");
        jdbcTemplate.update("delete from town");
        jdbcTemplate.update("delete from player");
        jdbcTemplate.update("delete from island_plot");
        jdbcTemplate.update("delete from island");
    }

    @Test
    void startingTheGameTwiceLeavesOneWorldOnePlayerAndOneTown() {
        newGameService.start(1L);
        newGameService.start(1L);

        assertThat(countRowsIn("island")).isEqualTo(20);
        assertThat(countRowsIn("player")).isEqualTo(1);
        assertThat(countRowsIn("town")).isEqualTo(1);
        assertThat(countRowsIn("town_plot")).isEqualTo(14);

        Player player = players.find().orElseThrow();
        Town town = towns.findByOwner(player.id()).orElseThrow();
        assertThat(town.plotNumber()).isEqualTo(1);

        World world = worlds.find().orElseThrow();
        Island capitalIsland = world.island(town.islandId());
        assertThat(capitalIsland.plots().get(0).occupant()).hasValue(town.id().value());

        assertThat(player.goldAmount()).isEqualTo(500);
        assertThat(town.resources().lastUpdate()).isEqualTo(NOW);
    }

    private int countRowsIn(String tableName) {
        Integer rowCount = jdbcTemplate.queryForObject("select count(*) from " + tableName, Integer.class);
        return rowCount == null ? 0 : rowCount;
    }

    @TestConfiguration
    static class FixedClockConfiguration {

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(NOW, ZoneOffset.UTC);
        }
    }
}
