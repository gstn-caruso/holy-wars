package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class BuildingSlotTest {

    @Test
    void emptySlotWhoseRequirementIsNotMetIsLocked() {
        BuildingSlot slot = new BuildingSlot(5, SlotKind.LAND, 2, Optional.empty());

        assertThat(slot.state(1)).isEqualTo(SlotState.LOCKED);
    }

    @Test
    void emptySlotWhoseRequirementIsExactlyMetIsFree() {
        BuildingSlot slot = new BuildingSlot(5, SlotKind.LAND, 2, Optional.empty());

        assertThat(slot.state(2)).isEqualTo(SlotState.FREE);
    }
}
