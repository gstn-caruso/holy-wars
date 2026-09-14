package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.world.IslandId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    @Test
    void townWithTownHallAtLevelThreeReportsThatLevelAndUnlocksSlotsUpToIt() {
        TownId id = new TownId(1);
        PlayerId ownerId = new PlayerId(1);
        PlotLocation location = new PlotLocation(new IslandId(3), 5);
        Town town = new Town(id, "Atenas", ownerId, location, BuildingSlots.standard(3));

        assertThat(town.townHallLevel()).isEqualTo(3);

        Map<Integer, SlotState> statesByPosition = town.slots().stream()
                .collect(Collectors.toMap(BuildingSlot::position, slot -> slot.state(town.townHallLevel())));

        assertThat(statesByPosition.get(1)).isEqualTo(SlotState.OCCUPIED);
        assertThat(statesByPosition.get(10)).isEqualTo(SlotState.LOCKED);
        assertThat(statesByPosition.get(11)).isEqualTo(SlotState.LOCKED);

        List<Integer> freePositions = List.of(2, 3, 4, 5, 6, 7, 8, 9, 12, 13, 14);
        freePositions.forEach(position -> assertThat(statesByPosition.get(position)).isEqualTo(SlotState.FREE));
    }
}
