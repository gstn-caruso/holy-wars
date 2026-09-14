package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.Building;
import holywars.town.BuildingType;
import holywars.town.Construction;
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

    @Test
    void savesATownWithASlotOccupiedByAnotherBuilding() {
        Town town = Town.founded(new TownId(4), new PlayerId(7), new IslandId(3), 1, "Tebas",
                LuxuryResource.WINE, FOUNDED_AT)
                .startingConstruction(2, BuildingType.WAREHOUSE, FOUNDED_AT)
                .advancedTo(FOUNDED_AT.plus(BuildingType.WAREHOUSE.buildTime()));

        townJpaAdapter.save(town);

        Town found = townJpaAdapter.find(new TownId(4)).orElseThrow();
        assertThat(found).isEqualTo(town);
        assertThat(found.slot(2).building()).contains(new Building(BuildingType.WAREHOUSE, 1));
    }

    @Test
    void savesATownWithASlotUnderConstructionAndKeepsMillisecondPrecision() {
        Instant startedAt = Instant.parse("2026-01-01T00:00:00.123Z");
        Town town = Town.founded(new TownId(5), new PlayerId(7), new IslandId(3), 1, "Delfos",
                LuxuryResource.MARBLE, startedAt)
                .startingConstruction(2, BuildingType.WAREHOUSE, startedAt);

        townJpaAdapter.save(town);

        Town found = townJpaAdapter.find(new TownId(5)).orElseThrow();
        assertThat(found).isEqualTo(town);
        assertThat(found.slot(2).construction()).contains(Construction.startingAt(BuildingType.WAREHOUSE, startedAt));
        assertThat(found.slot(2).construction().orElseThrow().startedAt()).isEqualTo(startedAt);
        assertThat(found.slot(2).construction().orElseThrow().finishesAt())
                .isEqualTo(startedAt.plus(BuildingType.WAREHOUSE.buildTime()));
    }

    @Test
    void savingATownAgainAfterStartingAConstructionUpdatesTheSlotRowInPlace() {
        Town town = Town.founded(new TownId(6), new PlayerId(7), new IslandId(3), 1, "Micenas",
                LuxuryResource.CRYSTAL, FOUNDED_AT);
        townJpaAdapter.save(town);

        Town underConstruction = town.startingConstruction(2, BuildingType.WAREHOUSE, FOUNDED_AT);
        townJpaAdapter.save(underConstruction);

        assertThat(townJpaAdapter.find(new TownId(6))).contains(underConstruction);
        assertThat(countBuildingSlotRowsFor(6L)).isEqualTo(14L);
    }

    private long countBuildingSlotRowsFor(long townId) {
        Long count = jdbcTemplate.queryForObject(
                "select count(*) from building_slot where town_id = ?", Long.class, townId);
        return count == null ? 0 : count;
    }
}
