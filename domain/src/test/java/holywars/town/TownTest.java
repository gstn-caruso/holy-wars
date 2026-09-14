package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.player.PlayerId;
import holywars.world.IslandId;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TownTest {

    @Test
    void foundedTownHasTheStandardBuildingSlotLayoutAndTownHallLevelOne() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas");

        assertThat(town.buildingSlots()).isEqualTo(BuildingSlots.standard());
        assertThat(town.townHallLevel()).isEqualTo(1);
    }

    @Test
    void slotReturnsTheBuildingSlotAtThatPosition() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas");

        assertThat(town.slot(1)).isEqualTo(BuildingSlots.standard().get(0));
    }

    @Test
    void slotAtANonExistentPositionIsRejected() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas");

        assertThatThrownBy(() -> town.slot(15))
                .isInstanceOf(InvalidBuildingSlotPositionException.class);
    }

    @Test
    void aTownWithoutExactlyItsFourteenDistinctPositionsIsRejected() {
        List<BuildingSlot> missingOnePosition = new ArrayList<>(BuildingSlots.standard());
        missingOnePosition.remove(missingOnePosition.size() - 1);

        assertThatThrownBy(() -> new Town(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                missingOnePosition))
                .isInstanceOf(InvalidBuildingSlotCountException.class);
    }

    @Test
    void aTownWithoutAnOccupiedTownHallIsRejected() {
        List<BuildingSlot> withoutAnOccupiedTownHall = new ArrayList<>(BuildingSlots.standard());
        withoutAnOccupiedTownHall.set(0, new BuildingSlot(1, BuildingSlotKind.TOWN_HALL, 1));

        assertThatThrownBy(() -> new Town(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                withoutAnOccupiedTownHall))
                .isInstanceOf(MissingTownHallException.class);
    }
}
