package holywars.server.view;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class CapitalHeaderViewTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void composesTheCapitalNameCoordinateAndResourceBar() {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(5, 8), "Naxos", LuxuryResource.WINE);
        Town capital = Town.founded(new TownId(9), new PlayerId(1), island.id(), 1, "Atenas", LuxuryResource.WINE,
                NOW);
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        ResourceBarView bar = ResourceBarView.of(capital, player, NOW);

        CapitalHeaderView header = CapitalHeaderView.of(island, capital, bar);

        assertThat(header.capitalName()).isEqualTo("Atenas");
        assertThat(header.islandCoordinateLabel()).isEqualTo("[5:8]");
        assertThat(header.resourceBar()).isEqualTo(bar);
    }

    @Test
    void exposesTheIslandAndTownIdsForTheViewButtons() {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(5, 8), "Naxos", LuxuryResource.WINE);
        Town capital = Town.founded(new TownId(9), new PlayerId(1), island.id(), 1, "Atenas", LuxuryResource.WINE,
                NOW);
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        ResourceBarView bar = ResourceBarView.of(capital, player, NOW);

        CapitalHeaderView header = CapitalHeaderView.of(island, capital, bar);

        assertThat(header.islandId()).isEqualTo(3L);
        assertThat(header.townId()).isEqualTo(9L);
    }
}
