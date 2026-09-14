package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BuildingSlotTest {

    @Test
    void aFreeSlotBelowTheRequiredTownHallLevelIsLocked() {
        BuildingSlot slot = new BuildingSlot(5, BuildingSlotKind.LAND, 2);

        assertThat(slot.state(1)).isEqualTo(BuildingSlotState.LOCKED);
    }

    @Test
    void aFreeSlotExactlyAtTheRequiredTownHallLevelIsFree() {
        BuildingSlot slot = new BuildingSlot(5, BuildingSlotKind.LAND, 2);

        assertThat(slot.state(2)).isEqualTo(BuildingSlotState.FREE);
    }

    @Test
    void aFreeSlotAboveTheRequiredTownHallLevelIsFree() {
        BuildingSlot slot = new BuildingSlot(5, BuildingSlotKind.LAND, 2);

        assertThat(slot.state(3)).isEqualTo(BuildingSlotState.FREE);
    }

    @Test
    void aBuiltSlotIsOccupiedEvenBelowItsRequiredTownHallLevel() {
        BuildingSlot slot = new BuildingSlot(5, BuildingSlotKind.LAND, 2, BuildingSlotKind.LAND, 1);

        assertThat(slot.state(1)).isEqualTo(BuildingSlotState.OCCUPIED);
    }

    @Test
    void aPositionOutsideOneToFourteenIsRejected() {
        assertThatThrownBy(() -> new BuildingSlot(15, BuildingSlotKind.LAND, 1))
                .isInstanceOf(InvalidBuildingSlotPositionException.class);
    }

    @Test
    void aRequiredTownHallLevelBelowOneIsRejected() {
        assertThatThrownBy(() -> new BuildingSlot(5, BuildingSlotKind.LAND, 0))
                .isInstanceOf(InvalidBuildingLevelException.class);
    }
}
