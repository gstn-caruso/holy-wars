package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import org.junit.jupiter.api.Test;

class IslandViewTest {

    @Test
    void arrangesTheSixteenPlotsInOrderWithTheOccupiedOneMarked() {
        Island island = Island.withFreePlots(new IslandId(5), new Coordinate(1, 2), "Naxos", LuxuryResource.WINE);
        island.plots().get(2).occupy(42L);

        IslandView view = IslandView.of(island, "Atenas", "Jugador");

        assertThat(view.id()).isEqualTo(5L);
        assertThat(view.name()).isEqualTo("Naxos");
        assertThat(view.x()).isEqualTo(1);
        assertThat(view.y()).isEqualTo(2);
        assertThat(view.luxuryResourceName()).isEqualTo("Vino");
        assertThat(view.luxuryIconPath()).isEqualTo("/img/resource-wine.svg");
        assertThat(view.plots()).hasSize(16);
        assertThat(view.plots().get(0).number()).isEqualTo(1);
        assertThat(view.plots().get(0).occupied()).isFalse();
        assertThat(view.plots().get(2).occupied()).isTrue();
        assertThat(view.plots().get(2).townName()).isEqualTo("Atenas");
        assertThat(view.plots().get(2).ownerName()).isEqualTo("Jugador");
        assertThat(view.plots().get(2).townId()).isEqualTo(42L);
    }
}
