package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.Optional;
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
        BuildingSlot slot = new BuildingSlot(5, BuildingSlotKind.LAND, 2, new Building(BuildingType.WAREHOUSE, 1));

        assertThat(slot.state(1)).isEqualTo(BuildingSlotState.OCCUPIED);
    }

    @Test
    void aSlotWithAConstructionInProgressIsUnderConstruction() {
        Construction construction = Construction.startingAt(BuildingType.WAREHOUSE, Instant.parse("2026-01-01T00:00:00Z"));
        BuildingSlot slot = new BuildingSlot(5, BuildingSlotKind.LAND, 2, Optional.empty(), Optional.of(construction));

        assertThat(slot.state(1)).isEqualTo(BuildingSlotState.UNDER_CONSTRUCTION);
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

    @Test
    void aBuildingOfAnotherKindThanTheSlotIsRejected() {
        assertThatThrownBy(
                () -> new BuildingSlot(12, BuildingSlotKind.WALL, 1, new Building(BuildingType.WAREHOUSE, 1)))
                .isInstanceOf(MismatchedBuildingTypeException.class);
    }

    @Test
    void aConstructionOfAnotherKindThanTheSlotIsRejected() {
        Construction construction = Construction.startingAt(BuildingType.WAREHOUSE, Instant.parse("2026-01-01T00:00:00Z"));

        assertThatThrownBy(
                () -> new BuildingSlot(12, BuildingSlotKind.WALL, 1, Optional.empty(), Optional.of(construction)))
                .isInstanceOf(MismatchedBuildingTypeException.class);
    }

    @Test
    void aBuildingAndAConstructionAtTheSameTimeAreRejected() {
        Construction construction = Construction.startingAt(BuildingType.WAREHOUSE, Instant.parse("2026-01-01T00:00:00Z"));
        Building building = new Building(BuildingType.WAREHOUSE, 1);

        assertThatThrownBy(() -> new BuildingSlot(5, BuildingSlotKind.LAND, 2, Optional.of(building),
                Optional.of(construction)))
                .isInstanceOf(ConflictingSlotContentsException.class);
    }

    @Test
    void anOccupiedTownHallSlotIsReportedAsAnOccupiedTownHall() {
        BuildingSlot slot = new BuildingSlot(1, BuildingSlotKind.TOWN_HALL, 1, new Building(BuildingType.TOWN_HALL, 1));

        assertThat(slot.isOccupiedTownHall()).isTrue();
    }

    @Test
    void aVacantTownHallSlotIsNotReportedAsAnOccupiedTownHall() {
        BuildingSlot slot = new BuildingSlot(1, BuildingSlotKind.TOWN_HALL, 1);

        assertThat(slot.isOccupiedTownHall()).isFalse();
    }

    @Test
    void anOccupiedLandSlotIsNotReportedAsAnOccupiedTownHall() {
        BuildingSlot slot = new BuildingSlot(5, BuildingSlotKind.LAND, 2, new Building(BuildingType.WAREHOUSE, 1));

        assertThat(slot.isOccupiedTownHall()).isFalse();
    }

    @Test
    void slotsWithTheSamePositionKindRequiredLevelAndBuildingAreEqual() {
        BuildingSlot vacant = new BuildingSlot(5, BuildingSlotKind.LAND, 2);
        BuildingSlot sameVacant = new BuildingSlot(5, BuildingSlotKind.LAND, 2);
        BuildingSlot built = new BuildingSlot(1, BuildingSlotKind.TOWN_HALL, 1, new Building(BuildingType.TOWN_HALL, 1));
        BuildingSlot sameBuilt = new BuildingSlot(1, BuildingSlotKind.TOWN_HALL, 1, new Building(BuildingType.TOWN_HALL, 1));

        assertThat(vacant).isEqualTo(sameVacant);
        assertThat(vacant.hashCode()).isEqualTo(sameVacant.hashCode());
        assertThat(built).isEqualTo(sameBuilt);
        assertThat(built.hashCode()).isEqualTo(sameBuilt.hashCode());
        assertThat(vacant).isNotEqualTo(built);
    }

    @Test
    void startingConstructionOnAFreeSlotOfTheRightKindAddsTheConstruction() {
        BuildingSlot slot = new BuildingSlot(5, BuildingSlotKind.LAND, 2);
        Instant startedAt = Instant.parse("2026-01-01T00:00:00Z");

        BuildingSlot underConstruction = slot.startingConstruction(BuildingType.WAREHOUSE, 2, startedAt);

        assertThat(underConstruction.construction())
                .contains(Construction.startingAt(BuildingType.WAREHOUSE, startedAt));
        assertThat(underConstruction.building()).isEmpty();
    }

    @Test
    void startingConstructionOnALockedSlotIsRejectedWithItsStateInTheMessage() {
        BuildingSlot slot = new BuildingSlot(5, BuildingSlotKind.LAND, 2);

        assertThatThrownBy(() -> slot.startingConstruction(BuildingType.WAREHOUSE, 1, Instant.parse("2026-01-01T00:00:00Z")))
                .isInstanceOf(SlotNotFreeException.class)
                .hasMessageContaining("LOCKED");
    }

    @Test
    void startingConstructionWithATypeOfAnotherKindThanTheSlotIsRejected() {
        BuildingSlot slot = new BuildingSlot(12, BuildingSlotKind.WALL, 1);

        assertThatThrownBy(() -> slot.startingConstruction(BuildingType.WAREHOUSE, 1, Instant.parse("2026-01-01T00:00:00Z")))
                .isInstanceOf(MismatchedBuildingTypeException.class);
    }
}
