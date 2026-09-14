package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class BuildingSlotsTest {

    @Test
    void standardLayoutHasFourteenSlotsInPositionOrder() {
        List<BuildingSlot> slots = BuildingSlots.standard(1);

        assertThat(slots).hasSize(14);
        assertThat(slots).extracting(BuildingSlot::position)
                .containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
    }
}
