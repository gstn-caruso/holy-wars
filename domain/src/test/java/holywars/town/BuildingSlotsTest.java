package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class BuildingSlotsTest {

    @Test
    void standardLayoutHasFourteenSlotsInPositionOrder() {
        List<BuildingSlot> slots = BuildingSlots.standard(1);

        assertThat(slots).hasSize(14);
        assertThat(slots).extracting(BuildingSlot::position)
                .containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
    }

    @Test
    void standardLayoutTownHallSlotIsOccupiedByATownHallOfTheGivenLevel() {
        List<BuildingSlot> slots = BuildingSlots.standard(3);

        BuildingSlot townHallSlot = slots.get(0);
        assertThat(townHallSlot.kind()).isEqualTo(SlotKind.TOWN_HALL);
        assertThat(townHallSlot.building()).isEqualTo(Optional.of(new Building(BuildingType.TOWN_HALL, 3)));
        assertThat(townHallSlot.state(1)).isEqualTo(SlotState.OCCUPIED);
    }
}
