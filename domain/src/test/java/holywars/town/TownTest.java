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

        Town town = Town.founded(id, "Atenas", ownerId, location);

        assertThat(town.id()).isEqualTo(id);
        assertThat(town.name()).isEqualTo("Atenas");
        assertThat(town.ownerId()).isEqualTo(ownerId);
        assertThat(town.location()).isEqualTo(location);
    }

    @Test
    void foundedTownHasATownHallAtLevelOne() {
        TownId id = new TownId(1);
        PlayerId ownerId = new PlayerId(1);
        PlotLocation location = new PlotLocation(new IslandId(3), 5);

        Town town = Town.founded(id, "Atenas", ownerId, location);

        assertThat(town.townHallLevel()).isEqualTo(1);
        assertThat(town.slots()).hasSize(14);
    }
}
