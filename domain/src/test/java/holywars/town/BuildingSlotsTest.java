package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class BuildingSlotsTest {

    private static final Map<Integer, BuildingSlotKind> EXPECTED_KINDS = Map.ofEntries(
            Map.entry(1, BuildingSlotKind.TOWN_HALL),
            Map.entry(2, BuildingSlotKind.LAND),
            Map.entry(3, BuildingSlotKind.LAND),
            Map.entry(4, BuildingSlotKind.LAND),
            Map.entry(5, BuildingSlotKind.LAND),
            Map.entry(6, BuildingSlotKind.LAND),
            Map.entry(7, BuildingSlotKind.LAND),
            Map.entry(8, BuildingSlotKind.LAND),
            Map.entry(9, BuildingSlotKind.LAND),
            Map.entry(10, BuildingSlotKind.LAND),
            Map.entry(11, BuildingSlotKind.LAND),
            Map.entry(12, BuildingSlotKind.WALL),
            Map.entry(13, BuildingSlotKind.COAST),
            Map.entry(14, BuildingSlotKind.COAST));

    private static final Map<Integer, Integer> EXPECTED_REQUIRED_LEVELS = Map.ofEntries(
            Map.entry(1, 1),
            Map.entry(2, 1),
            Map.entry(3, 1),
            Map.entry(4, 1),
            Map.entry(5, 2),
            Map.entry(6, 2),
            Map.entry(7, 3),
            Map.entry(8, 3),
            Map.entry(9, 3),
            Map.entry(10, 4),
            Map.entry(11, 4),
            Map.entry(12, 1),
            Map.entry(13, 1),
            Map.entry(14, 1));

    @Test
    void standardLayoutHasFourteenSlotsWithTheExactKindAndRequiredLevelFromTheTable() {
        List<BuildingSlot> slots = BuildingSlots.standard();

        assertThat(slots).hasSize(14);
        for (BuildingSlot slot : slots) {
            assertThat(slot.kind())
                    .as("kind of slot %d", slot.position())
                    .isEqualTo(EXPECTED_KINDS.get(slot.position()));
            assertThat(slot.requiredTownHallLevel())
                    .as("required level of slot %d", slot.position())
                    .isEqualTo(EXPECTED_REQUIRED_LEVELS.get(slot.position()));
        }
    }
}
