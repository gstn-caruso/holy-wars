package holywars.server.game;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownRepository;
import holywars.world.Island;
import holywars.world.World;
import holywars.world.WorldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class NewGameServiceTest {

    @Autowired
    private NewGameService newGameService;

    @Autowired
    private WorldRepository worldRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void emptyTheSharedDatabase() {
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

        Player player = playerRepository.find().orElseThrow();
        Town town = townRepository.findByOwner(player.id()).orElseThrow();
        assertThat(town.plotNumber()).isEqualTo(1);

        World world = worldRepository.find().orElseThrow();
        Island capitalIsland = world.island(town.islandId());
        assertThat(capitalIsland.plots().get(0).occupant()).hasValue(town.id().value());
    }

    private int countRowsIn(String tableName) {
        Integer rowCount = jdbcTemplate.queryForObject("select count(*) from " + tableName, Integer.class);
        return rowCount == null ? 0 : rowCount;
    }
}
