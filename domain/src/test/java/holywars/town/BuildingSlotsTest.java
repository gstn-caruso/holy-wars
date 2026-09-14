package holywars.town;

import static holywars.town.BuildingSlotKind.COAST;
import static holywars.town.BuildingSlotKind.LAND;
import static holywars.town.BuildingSlotKind.TOWN_HALL;
import static holywars.town.BuildingSlotKind.WALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.Test;

class BuildingSlotsTest {

    @Test
    void standardLayoutHasTheFirstSlotOccupiedByALevelOneTownHall() {
        List<BuildingSlot> slots = BuildingSlots.standard();

        BuildingSlot townHallSlot = slots.get(0);
        assertThat(townHallSlot.position()).isEqualTo(1);
        assertThat(townHallSlot.isOccupied()).isTrue();
        assertThat(townHallSlot.builtLevel()).hasValue(1);
    }

    @Test
    void standardLayoutHasFourteenSlotsWithTheExactKindAndRequiredLevelFromTheTable() {
        List<BuildingSlot> slots = BuildingSlots.standard();

        assertThat(slots)
                .extracting(BuildingSlot::position, BuildingSlot::kind, BuildingSlot::requiredTownHallLevel)
                .containsExactly(
                        tuple(1, TOWN_HALL, 1),
                        tuple(2, LAND, 1),
                        tuple(3, LAND, 1),
                        tuple(4, LAND, 1),
                        tuple(5, LAND, 2),
                        tuple(6, LAND, 2),
                        tuple(7, LAND, 3),
                        tuple(8, LAND, 3),
                        tuple(9, LAND, 3),
                        tuple(10, LAND, 4),
                        tuple(11, LAND, 4),
                        tuple(12, WALL, 1),
                        tuple(13, COAST, 1),
                        tuple(14, COAST, 1));
    }
}
