package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
class TownJpaAdapterTest {

    private static final Instant FOUNDED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private TownJpaRepository townJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private TownJpaAdapter townJpaAdapter;

    @BeforeEach
    void setUp() {
        townJpaAdapter = new TownJpaAdapter(townJpaRepository);
    }

    @Test
    void savesATownAndFindsItByIdAndByOwner() {
        Town town = Town.founded(new TownId(1), new PlayerId(7), new IslandId(3), 1, "Atenas",
                LuxuryResource.MARBLE, FOUNDED_AT);

        townJpaAdapter.save(town);

        assertThat(townJpaAdapter.find(new TownId(1))).contains(town);
        assertThat(townJpaAdapter.findByOwner(new PlayerId(7))).contains(town);
    }

    @Test
    void savesAnAdvancedTownAndKeepsItsWoodLuxuryAndResourcesUpdatedAt() {
        Town town = Town.founded(new TownId(2), new PlayerId(7), new IslandId(3), 1, "Esparta",
                LuxuryResource.CRYSTAL, FOUNDED_AT)
                .advancedTo(FOUNDED_AT.plus(Duration.ofHours(1)));

        townJpaAdapter.save(town);

        assertThat(townJpaAdapter.find(new TownId(2))).contains(town);
    }

    @Test
    void savesAFoundedTownWithAllFourteenBuildingSlotsPersisted() {
        Town town = Town.founded(new TownId(3), new PlayerId(7), new IslandId(3), 1, "Corinto",
                LuxuryResource.SULFUR, FOUNDED_AT);

        townJpaAdapter.save(town);

        assertThat(townJpaAdapter.find(new TownId(3))).contains(town);
        assertThat(countBuildingSlotRowsFor(3L)).isEqualTo(14L);
    }

    private long countBuildingSlotRowsFor(long townId) {
        Long count = jdbcTemplate.queryForObject(
                "select count(*) from building_slot where town_id = ?", Long.class, townId);
        return count == null ? 0 : count;
    }
}
