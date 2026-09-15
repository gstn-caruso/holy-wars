package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class TownPlotTest {

    @Test
    void aFreePlotBelowTheRequiredTownHallLevelIsLocked() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2);

        assertThat(plot.state(1)).isEqualTo(TownPlotState.LOCKED);
    }

    @Test
    void aFreePlotExactlyAtTheRequiredTownHallLevelIsFree() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2);

        assertThat(plot.state(2)).isEqualTo(TownPlotState.FREE);
    }

    @Test
    void aFreePlotAboveTheRequiredTownHallLevelIsFree() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2);

        assertThat(plot.state(3)).isEqualTo(TownPlotState.FREE);
    }

    @Test
    void aBuiltPlotIsOccupiedEvenBelowItsRequiredTownHallLevel() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2, new Building(BuildingType.WAREHOUSE, 1));

        assertThat(plot.state(1)).isEqualTo(TownPlotState.OCCUPIED);
    }

    @Test
    void aPlotWithAConstructionInProgressIsUnderConstruction() {
        Construction construction = Construction.startingAt(BuildingType.WAREHOUSE, Instant.parse("2026-01-01T00:00:00Z"));
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2, Optional.empty(), Optional.of(construction));

        assertThat(plot.state(1)).isEqualTo(TownPlotState.UNDER_CONSTRUCTION);
    }

    @Test
    void aPositionOutsideOneToFourteenIsRejected() {
        assertThatThrownBy(() -> new TownPlot(15, TownPlotKind.LAND, 1))
                .isInstanceOf(InvalidTownPlotPositionException.class);
    }

    @Test
    void aRequiredTownHallLevelBelowOneIsRejected() {
        assertThatThrownBy(() -> new TownPlot(5, TownPlotKind.LAND, 0))
                .isInstanceOf(InvalidBuildingLevelException.class);
    }

    @Test
    void aBuildingOfAnotherKindThanThePlotIsRejected() {
        assertThatThrownBy(
                () -> new TownPlot(12, TownPlotKind.WALL, 1, new Building(BuildingType.WAREHOUSE, 1)))
                .isInstanceOf(MismatchedBuildingTypeException.class);
    }

    @Test
    void aConstructionOfAnotherKindThanThePlotIsRejected() {
        Construction construction = Construction.startingAt(BuildingType.WAREHOUSE, Instant.parse("2026-01-01T00:00:00Z"));

        assertThatThrownBy(
                () -> new TownPlot(12, TownPlotKind.WALL, 1, Optional.empty(), Optional.of(construction)))
                .isInstanceOf(MismatchedBuildingTypeException.class);
    }

    @Test
    void aBuildingAndAConstructionAtTheSameTimeAreRejected() {
        Construction construction = Construction.startingAt(BuildingType.WAREHOUSE, Instant.parse("2026-01-01T00:00:00Z"));
        Building building = new Building(BuildingType.WAREHOUSE, 1);

        assertThatThrownBy(() -> new TownPlot(5, TownPlotKind.LAND, 2, Optional.of(building),
                Optional.of(construction)))
                .isInstanceOf(ConflictingTownPlotContentsException.class);
    }

    @Test
    void anOccupiedTownHallPlotIsReportedAsAnOccupiedTownHall() {
        TownPlot plot = new TownPlot(1, TownPlotKind.TOWN_HALL, 1, new Building(BuildingType.TOWN_HALL, 1));

        assertThat(plot.isOccupiedTownHall()).isTrue();
    }

    @Test
    void aVacantTownHallPlotIsNotReportedAsAnOccupiedTownHall() {
        TownPlot plot = new TownPlot(1, TownPlotKind.TOWN_HALL, 1);

        assertThat(plot.isOccupiedTownHall()).isFalse();
    }

    @Test
    void anOccupiedLandPlotIsNotReportedAsAnOccupiedTownHall() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2, new Building(BuildingType.WAREHOUSE, 1));

        assertThat(plot.isOccupiedTownHall()).isFalse();
    }

    @Test
    void builtLevelReturnsTheLevelOfTheBuiltBuilding() {
        TownPlot plot = new TownPlot(1, TownPlotKind.TOWN_HALL, 1, new Building(BuildingType.TOWN_HALL, 3));

        assertThat(plot.builtLevel()).isEqualTo(3);
    }

    @Test
    void plotsWithTheSamePositionKindRequiredLevelAndBuildingAreEqual() {
        TownPlot vacant = new TownPlot(5, TownPlotKind.LAND, 2);
        TownPlot sameVacant = new TownPlot(5, TownPlotKind.LAND, 2);
        TownPlot built = new TownPlot(1, TownPlotKind.TOWN_HALL, 1, new Building(BuildingType.TOWN_HALL, 1));
        TownPlot sameBuilt = new TownPlot(1, TownPlotKind.TOWN_HALL, 1, new Building(BuildingType.TOWN_HALL, 1));

        assertThat(vacant).isEqualTo(sameVacant);
        assertThat(vacant.hashCode()).isEqualTo(sameVacant.hashCode());
        assertThat(built).isEqualTo(sameBuilt);
        assertThat(built.hashCode()).isEqualTo(sameBuilt.hashCode());
        assertThat(vacant).isNotEqualTo(built);
    }

    @Test
    void startingConstructionOnAFreePlotOfTheRightKindAddsTheConstruction() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2);
        Instant startedAt = Instant.parse("2026-01-01T00:00:00Z");

        TownPlot underConstruction = plot.startingConstruction(BuildingType.WAREHOUSE, 2, startedAt);

        assertThat(underConstruction.construction())
                .contains(Construction.startingAt(BuildingType.WAREHOUSE, startedAt));
        assertThat(underConstruction.building()).isEmpty();
    }

    @Test
    void startingConstructionOnALockedPlotIsRejectedWithItsStateInTheMessage() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2);

        assertThatThrownBy(() -> plot.startingConstruction(BuildingType.WAREHOUSE, 1, Instant.parse("2026-01-01T00:00:00Z")))
                .isInstanceOf(TownPlotNotFreeException.class)
                .hasMessageContaining("LOCKED");
    }

    @Test
    void startingConstructionWithATypeOfAnotherKindThanThePlotIsRejected() {
        TownPlot plot = new TownPlot(12, TownPlotKind.WALL, 1);

        assertThatThrownBy(() -> plot.startingConstruction(BuildingType.WAREHOUSE, 1, Instant.parse("2026-01-01T00:00:00Z")))
                .isInstanceOf(MismatchedBuildingTypeException.class);
    }

    @Test
    void advancedToCompletesAnOverdueConstructionAsALevelOneBuilding() {
        Instant startedAt = Instant.parse("2026-01-01T00:00:00Z");
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2).startingConstruction(BuildingType.WAREHOUSE,
                2, startedAt);

        TownPlot advanced = plot.advancedTo(startedAt.plus(BuildingType.WAREHOUSE.buildTime()));

        assertThat(advanced.building()).contains(new Building(BuildingType.WAREHOUSE, 1));
        assertThat(advanced.construction()).isEmpty();
    }

    @Test
    void advancedToDoesNotTouchAConstructionStillInProgress() {
        Instant startedAt = Instant.parse("2026-01-01T00:00:00Z");
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2).startingConstruction(BuildingType.WAREHOUSE,
                2, startedAt);

        TownPlot advanced = plot.advancedTo(startedAt.plus(BuildingType.WAREHOUSE.buildTime()).minusMillis(1));

        assertThat(advanced).isEqualTo(plot);
    }

    @Test
    void advancedToDoesNotTouchAPlotWithoutAConstruction() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2);

        TownPlot advanced = plot.advancedTo(Instant.parse("2026-01-01T00:00:00Z"));

        assertThat(advanced).isEqualTo(plot);
    }

    @Test
    void allowedTypesOnAFreePlotAreTheTypesOfItsKind() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2);

        assertThat(plot.allowedTypes(2)).isEqualTo(BuildingType.allowedFor(TownPlotKind.LAND));
    }

    @Test
    void allowedTypesOnALockedPlotAreEmpty() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2);

        assertThat(plot.allowedTypes(1)).isEmpty();
    }

    @Test
    void allowedTypesOnAnOccupiedPlotAreEmpty() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2, new Building(BuildingType.WAREHOUSE, 1));

        assertThat(plot.allowedTypes(2)).isEmpty();
    }

    @Test
    void allowedTypesOnAPlotUnderConstructionAreEmpty() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2)
                .startingConstruction(BuildingType.WAREHOUSE, 2, Instant.parse("2026-01-01T00:00:00Z"));

        assertThat(plot.allowedTypes(2)).isEmpty();
    }
}
