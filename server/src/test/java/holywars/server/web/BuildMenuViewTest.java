package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.BuildingType;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class BuildMenuViewTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void freeSlotListsTheAllowedTypesWithNameSpriteCostsAndMinutes() {
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
    }
}
