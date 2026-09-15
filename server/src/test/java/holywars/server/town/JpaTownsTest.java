package holywars.server.town;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.Building;
import holywars.town.TownPlots;
import holywars.town.BuildingType;
import holywars.town.Construction;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import jakarta.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
class JpaTownsTest {

    private static final Instant FOUNDED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private JpaTownTable townJpaTable;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    private JpaTowns towns;

    @BeforeEach
    void setUp() {
        towns = new JpaTowns(townJpaTable);
    }

    @Test
    void savesATownAndFindsItByIdAndByOwner() {
        Town town = Town.founded(new TownId(1), new PlayerId(7), new IslandId(3), 1, "Atenas",
                LuxuryResource.MARBLE, FOUNDED_AT);

        towns.save(town);

        assertThat(towns.find(new TownId(1))).contains(town);
        assertThat(towns.findByOwner(new PlayerId(7))).contains(town);
    }

    @Test
    void savesAnAdvancedTownAndKeepsItsWoodLuxuryAndResourcesUpdatedAt() {
        Town town = Town.founded(new TownId(2), new PlayerId(7), new IslandId(3), 1, "Esparta",
                LuxuryResource.CRYSTAL, FOUNDED_AT)
                .advancedTo(FOUNDED_AT.plus(Duration.ofHours(1)));

        towns.save(town);

        assertThat(towns.find(new TownId(2))).contains(town);
    }

    @Test
    void savesAFoundedTownWithAllFourteenTownPlotsPersisted() {
        Town town = Town.founded(new TownId(3), new PlayerId(7), new IslandId(3), 1, "Corinto",
                LuxuryResource.SULFUR, FOUNDED_AT);

        towns.save(town);

        assertThat(towns.find(new TownId(3))).contains(town);
        assertThat(countTownPlotRowsFor(3L)).isEqualTo(14L);
    }

    @Test
    void savesATownWithAPlotOccupiedByAnotherBuilding() {
        Town town = Town.founded(new TownId(4), new PlayerId(7), new IslandId(3), 1, "Tebas",
                LuxuryResource.WINE, FOUNDED_AT)
                .startingConstruction(2, BuildingType.WAREHOUSE, FOUNDED_AT)
                .advancedTo(FOUNDED_AT.plus(BuildingType.WAREHOUSE.buildTime()));

        towns.save(town);

        Town found = towns.find(new TownId(4)).orElseThrow();
        assertThat(found).isEqualTo(town);
        assertThat(found.plot(2).building()).contains(new Building(BuildingType.WAREHOUSE, 1));
    }

    @Test
    void savesATownWithAPlotUnderConstructionAndKeepsMillisecondPrecision() {
        Instant startedAt = Instant.parse("2026-01-01T00:00:00.123Z");
        Town town = Town.founded(new TownId(5), new PlayerId(7), new IslandId(3), 1, "Delfos",
                LuxuryResource.MARBLE, startedAt)
                .startingConstruction(2, BuildingType.WAREHOUSE, startedAt);

        towns.save(town);

        Town found = towns.find(new TownId(5)).orElseThrow();
        assertThat(found).isEqualTo(town);
        assertThat(found.plot(2).construction()).contains(Construction.startingAt(BuildingType.WAREHOUSE, startedAt));
        assertThat(found.plot(2).construction().orElseThrow().startedAt()).isEqualTo(startedAt);
        assertThat(found.plot(2).construction().orElseThrow().finishesAt())
                .isEqualTo(startedAt.plus(BuildingType.WAREHOUSE.buildTime()));
    }

    @Test
    void savingATownAgainAfterStartingAConstructionUpdatesThePlotRowInPlace() {
        Town town = Town.founded(new TownId(6), new PlayerId(7), new IslandId(3), 1, "Micenas",
                LuxuryResource.CRYSTAL, FOUNDED_AT);
        towns.save(town);

        Town underConstruction = town.startingConstruction(2, BuildingType.WAREHOUSE, FOUNDED_AT);
        towns.save(underConstruction);

        assertThat(towns.find(new TownId(6))).contains(underConstruction);
        assertThat(countTownPlotRowsFor(6L)).isEqualTo(14L);
    }

    @Test
    void findsATownWithItsTownPlotsInASingleQuery() {
        Town town = Town.founded(new TownId(7), new PlayerId(9), new IslandId(3), 1, "Cnosos",
                LuxuryResource.WINE, FOUNDED_AT);
        towns.save(town);
        entityManager.flush();
        entityManager.clear();
        Statistics statistics = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class).getStatistics();

        statistics.clear();
        assertThat(towns.find(new TownId(7))).contains(town);
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1L);

        statistics.clear();
        assertThat(towns.findByOwner(new PlayerId(9))).contains(town);
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1L);
    }

    @Test
    void reconstructsTheStandardLayoutForATownWithoutPersistedPlotsAndBackfillsOnSave() {
        jdbcTemplate.update(
                "insert into town (id, island_id, luxury_resource, luxury_ticks, name, owner_id, plot_number, "
                        + "resources_updated_at, wood_ticks) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                8L, 3L, LuxuryResource.WINE.name(), 100L, "Rodas", 7L, 1, Timestamp.from(FOUNDED_AT), 500L);

        Town found = towns.find(new TownId(8)).orElseThrow();
        assertThat(found.townPlots()).isEqualTo(TownPlots.standard());
        assertThat(countTownPlotRowsFor(8L)).isZero();

        towns.save(found);

        assertThat(countTownPlotRowsFor(8L)).isEqualTo(14L);
    }

    private long countTownPlotRowsFor(long townId) {
        Long count = jdbcTemplate.queryForObject(
                "select count(*) from town_plot where town_id = ?", Long.class, townId);
        return count == null ? 0 : count;
    }
}
