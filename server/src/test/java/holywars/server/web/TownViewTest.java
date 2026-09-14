package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class TownViewTest {

    @Test
    void arrangesOwnerIslandAndPlotNumber() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(7), 3, "Atenas",
                LuxuryResource.WINE, Instant.now());

        TownView view = TownView.of(town, "Jugador", "Naxos");

        assertThat(view.id()).isEqualTo(1L);
        assertThat(view.name()).isEqualTo("Atenas");
        assertThat(view.ownerName()).isEqualTo("Jugador");
        assertThat(view.islandId()).isEqualTo(7L);
        assertThat(view.islandName()).isEqualTo("Naxos");
        assertThat(view.plotNumber()).isEqualTo(3);
    }
}
