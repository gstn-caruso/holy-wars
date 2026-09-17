package holywars.server.town.view;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.BuildingType;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class BuildMenuViewTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void freePlotListsTheAllowedTypesWithNameSpriteCostsAndMinutes() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, NOW);

        BuildMenuView menu = BuildMenuView.of(town, 2, NOW);

        assertThat(menu.title()).isEqualTo("Parcela 2");
        BuildOptionView warehouse = menu.options().stream()
                .filter(option -> option.type().equals("WAREHOUSE"))
                .findFirst()
                .orElseThrow();
        assertThat(warehouse.name()).isEqualTo("Almacén");
        assertThat(warehouse.spriteHref()).isEqualTo("/img/building-warehouse.svg");
        assertThat(warehouse.woodCost()).isEqualTo(40);
        assertThat(warehouse.luxuryCost()).isEqualTo(0);
        assertThat(warehouse.minutes()).isEqualTo(6);
        assertThat(warehouse.luxuryIconPath()).isNull();
    }

    @Test
    void freePlotOptionWithALuxuryCostCarriesTheTownsLuxuryResourceIcon() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, NOW);

        BuildMenuView menu = BuildMenuView.of(town, 2, NOW);

        BuildOptionView tavern = menu.options().stream()
                .filter(option -> option.type().equals("TAVERN"))
                .findFirst()
                .orElseThrow();
        assertThat(tavern.luxuryCost()).isEqualTo(10);
        assertThat(tavern.luxuryIconPath()).isEqualTo("/img/resource-wine.svg");
    }

    @Test
    void lockedPlotShowsTheRequiredTownHallLevel() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, NOW);

        BuildMenuView menu = BuildMenuView.of(town, 5, NOW);

        assertThat(menu.statusText()).isEqualTo("Requiere ayuntamiento nivel 2");
        assertThat(menu.options()).isEmpty();
    }

    @Test
    void occupiedPlotShowsTheBuildingsSpanishNameAndLevel() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, NOW);

        BuildMenuView menu = BuildMenuView.of(town, 1, NOW);

        assertThat(menu.statusText()).isEqualTo("Ayuntamiento nivel 1");
        assertThat(menu.options()).isEmpty();
    }

    @Test
    void underConstructionPlotShowsTheRemainingMinutesRoundedUp() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, NOW)
                .startingConstruction(2, BuildingType.WAREHOUSE, NOW);
        Instant fiveMinutesAndAMillisecondBeforeFinishing = NOW.plus(BuildingType.WAREHOUSE.buildTime())
                .minus(Duration.ofMinutes(5)).minusMillis(1);

        BuildMenuView menu = BuildMenuView.of(town, 2, fiveMinutesAndAMillisecondBeforeFinishing);

        assertThat(menu.statusText()).isEqualTo("En obra: Almacén · faltan 6 min");
        assertThat(menu.options()).isEmpty();
    }
}
