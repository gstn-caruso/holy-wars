package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.world.IslandId;
import org.junit.jupiter.api.Test;

class TownTest {

    @Test
    void townKnowsItsNameOwnerAndLocation() {
        TownId id = new TownId(1);
        PlayerId ownerId = new PlayerId(1);
        PlotLocation location = new PlotLocation(new IslandId(3), 5);

        Town town = new Town(id, "Atenas", ownerId, location);

        assertThat(town.id()).isEqualTo(id);
        assertThat(town.name()).isEqualTo("Atenas");
        assertThat(town.ownerId()).isEqualTo(ownerId);
        assertThat(town.location()).isEqualTo(location);
    }
}
