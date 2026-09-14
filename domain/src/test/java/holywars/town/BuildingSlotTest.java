package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

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
}
