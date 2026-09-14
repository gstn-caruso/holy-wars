package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.world.IslandId;
import org.junit.jupiter.api.Test;

class TownTest {

    @Test
    void foundedTownHasTheStandardBuildingSlotLayoutAndTownHallLevelOne() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas");

        assertThat(town.buildingSlots()).isEqualTo(BuildingSlots.standard());
        assertThat(town.townHallLevel()).isEqualTo(1);
    }
}
