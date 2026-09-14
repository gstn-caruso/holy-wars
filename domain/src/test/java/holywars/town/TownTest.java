package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.player.PlayerId;
import holywars.world.IslandId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class TownTest {

    private final TownId id = new TownId(1);
    private final PlayerId ownerId = new PlayerId(1);
    private final PlotLocation location = new PlotLocation(new IslandId(3), 5);

    @Test
    void townKnowsItsNameOwnerAndLocation() {
        Town town = Town.founded(id, "Atenas", ownerId, location);

        assertThat(town.id()).isEqualTo(id);
        assertThat(town.name()).isEqualTo("Atenas");
        assertThat(town.ownerId()).isEqualTo(ownerId);
        assertThat(town.location()).isEqualTo(location);
    }

    @Test
    void foundedTownHasATownHallAtLevelOne() {
        Town town = Town.founded(id, "Atenas", ownerId, location);

        assertThat(town.townHallLevel()).isEqualTo(1);
        assertThat(town.slots()).hasSize(14);
    }

    @Test
    void townWithTownHallAtLevelThreeReportsThatLevelAndUnlocksSlotsUpToIt() {
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

    @Test
    void townRejectsASlotListThatIsNotTheFourteenPositions() {
        List<BuildingSlot> thirteenSlots = BuildingSlots.standard(1).subList(0, 13);
        assertThatThrownBy(() -> new Town(id, "Atenas", ownerId, location, thirteenSlots))
                .isInstanceOf(InvalidBuildingSlotCountException.class);

        List<BuildingSlot> fourteenWithDuplicatePosition = new ArrayList<>(BuildingSlots.standard(1));
        fourteenWithDuplicatePosition.set(1, new BuildingSlot(1, SlotKind.LAND, 1, Optional.empty()));
        assertThatThrownBy(() -> new Town(id, "Atenas", ownerId, location, fourteenWithDuplicatePosition))
                .isInstanceOf(InvalidBuildingSlotCountException.class);
    }

    @Test
    void townRejectsSlotsWithoutAnOccupiedTownHall() {
        List<BuildingSlot> slotsWithEmptyTownHall = new ArrayList<>(BuildingSlots.standard(1));
        slotsWithEmptyTownHall.set(0, new BuildingSlot(1, SlotKind.TOWN_HALL, 1, Optional.empty()));

        assertThatThrownBy(() -> new Town(id, "Atenas", ownerId, location, slotsWithEmptyTownHall))
                .isInstanceOf(MissingTownHallException.class);
    }
}
