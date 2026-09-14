package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void slotUnlocksExactlyAtItsRequiredTownHallLevel(int requiredLevel) {
        BuildingSlot slot = new BuildingSlot(5, SlotKind.LAND, requiredLevel, Optional.empty());

        assertThat(slot.state(requiredLevel - 1)).isEqualTo(SlotState.LOCKED);
        assertThat(slot.state(requiredLevel)).isEqualTo(SlotState.FREE);
    }

    @Test
    void occupiedSlotStaysOccupiedEvenWhenItsRequirementExceedsTheTownHallLevel() {
        Building academy = new Building(BuildingType.ACADEMY, 1);
        BuildingSlot slot = new BuildingSlot(5, SlotKind.LAND, 4, Optional.of(academy));

        assertThat(slot.state(1)).isEqualTo(SlotState.OCCUPIED);
    }

    @Test
    void slotRejectsABuildingOfATypeThatDoesNotBelongToItsKind() {
        Building wall = new Building(BuildingType.WALL, 1);
        assertThatThrownBy(() -> new BuildingSlot(5, SlotKind.LAND, 1, Optional.of(wall)))
                .isInstanceOf(MismatchedBuildingTypeException.class);

        Building academy = new Building(BuildingType.ACADEMY, 1);
        assertThatThrownBy(() -> new BuildingSlot(13, SlotKind.COAST, 1, Optional.of(academy)))
                .isInstanceOf(MismatchedBuildingTypeException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 15})
    void slotRejectsAPositionOutsideOneToFourteen(int invalidPosition) {
        assertThatThrownBy(() -> new BuildingSlot(invalidPosition, SlotKind.LAND, 1, Optional.empty()))
                .isInstanceOf(InvalidBuildingSlotPositionException.class);
    }
}
