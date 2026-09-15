package holywars.server.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import holywars.player.PlayerId;
import holywars.town.TownPlotState;
import holywars.town.BuildingType;
import holywars.town.TownPlotNotFreeException;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ConstructionServiceTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private ConstructionService constructionService;

    @Autowired
    private Towns towns;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockitoBean
    private TownClockwork clockwork;

    @BeforeEach
    void emptyTheSharedDatabase() {
        jdbcTemplate.update("delete from town_plot");
        jdbcTemplate.update("delete from town");
        jdbcTemplate.update("delete from player");
        jdbcTemplate.update("delete from island_plot");
        jdbcTemplate.update("delete from island");
    }

    @Test
    void startingAConstructionPersistsTheUnderConstructionPlotAndTheSpentWood() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        towns.save(town);

        constructionService.start(new TownId(1), 2, BuildingType.WAREHOUSE);

        Town found = towns.find(new TownId(1)).orElseThrow();
        assertThat(found.plot(2).state(1)).isEqualTo(TownPlotState.UNDER_CONSTRUCTION);
        assertThat(found.resources().woodAmount()).isEqualTo(500 - BuildingType.WAREHOUSE.woodCost());
    }

    @Test
    void startingAConstructionSchedulesTheClockworkFinishForTheSavedTown() {
        Town town = Town.founded(new TownId(3), new PlayerId(1), new IslandId(1), 1, "Corinto",
                LuxuryResource.WINE, NOW);
        towns.save(town);

        Town updated = constructionService.start(new TownId(3), 2, BuildingType.WAREHOUSE);

        verify(clockwork).scheduleFinish(updated);
    }

    @Test
    void startingAConstructionForAnUnknownTownThrowsUnknownTown() {
        assertThatThrownBy(() -> constructionService.start(new TownId(404), 2, BuildingType.WAREHOUSE))
                .isInstanceOf(UnknownTownException.class);
    }

    @Test
    void startingAConstructionOnANonFreePlotPropagatesWithoutPersisting() {
        Town town = Town.founded(new TownId(2), new PlayerId(1), new IslandId(1), 1, "Esparta",
                LuxuryResource.WINE, NOW);
        towns.save(town);

        assertThatThrownBy(() -> constructionService.start(new TownId(2), 1, BuildingType.WAREHOUSE))
                .isInstanceOf(TownPlotNotFreeException.class);

        assertThat(towns.find(new TownId(2))).contains(town);
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
