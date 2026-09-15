package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.player.PlayerId;
import holywars.resources.NotEnoughResourcesException;
import holywars.resources.TownResources;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TownTest {

    private static final Instant FOUNDED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void foundedTownHasTheStandardBuildingPlotLayoutAndTownHallLevelOne() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThat(town.plots()).isEqualTo(TownPlots.standard());
        assertThat(town.townHallLevel()).isEqualTo(1);
    }

    @Test
    void foundedTownStartsWithFiveHundredWoodAndOneHundredOfTheIslandsLuxury() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.MARBLE, FOUNDED_AT);

        assertThat(town.resources().woodAmount()).isEqualTo(500);
        assertThat(town.resources().luxuryAmount()).isEqualTo(100);
        assertThat(town.resources().luxuryResource()).isEqualTo(LuxuryResource.MARBLE);
    }

    @Test
    void advancedToMovesTheTownsResourcesForward() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);
        Instant anHourLater = FOUNDED_AT.plus(Duration.ofHours(1));

        Town advanced = town.advancedTo(anHourLater);

        assertThat(advanced.resources().woodAmount()).isEqualTo(530);
        assertThat(advanced.resources().lastUpdate()).isEqualTo(anHourLater);
    }

    @Test
    void spendReducesTheTownsWoodAndLuxury() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        Town spent = town.spend(100, 20);

        assertThat(spent.resources().woodAmount()).isEqualTo(400);
        assertThat(spent.resources().luxuryAmount()).isEqualTo(80);
    }

    @Test
    void plotReturnsTheBuildingPlotAtThatPosition() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThat(town.plot(1)).isEqualTo(TownPlots.standard().get(0));
    }

    @Test
    void plotAtANonExistentPositionIsRejected() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThatThrownBy(() -> town.plot(15))
                .isInstanceOf(InvalidTownPlotPositionException.class);
    }

    @Test
    void aTownWithoutExactlyItsFourteenDistinctPositionsIsRejected() {
        List<TownPlot> missingOnePosition = new ArrayList<>(TownPlots.standard());
        missingOnePosition.remove(missingOnePosition.size() - 1);

        assertThatThrownBy(() -> new Town(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                missingOnePosition, TownResources.starting(LuxuryResource.WINE, FOUNDED_AT)))
                .isInstanceOf(InvalidTownPlotCountException.class);
    }

    @Test
    void aTownWithARepeatedBuildingPlotPositionIsRejected() {
        List<TownPlot> repeatedPosition = new ArrayList<>(TownPlots.standard());
        repeatedPosition.set(13, new TownPlot(13, TownPlotKind.COAST, 1));

        assertThatThrownBy(() -> new Town(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                repeatedPosition, TownResources.starting(LuxuryResource.WINE, FOUNDED_AT)))
                .isInstanceOf(InvalidTownPlotCountException.class)
                .hasMessage("A town must have exactly 14 distinct building plot positions, got 13");
    }

    @Test
    void startingConstructionSpendsResourcesAndLeavesTheConstruction() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        Town underConstruction = town.startingConstruction(2, BuildingType.WAREHOUSE, FOUNDED_AT);

        assertThat(underConstruction.resources().woodAmount()).isEqualTo(460);
        assertThat(underConstruction.resources().luxuryAmount()).isEqualTo(100);
        assertThat(underConstruction.plot(2).construction())
                .contains(Construction.startingAt(BuildingType.WAREHOUSE, FOUNDED_AT));
    }

    @Test
    void startingConstructionAtANonExistentPositionChangesNothing() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThatThrownBy(() -> town.startingConstruction(15, BuildingType.WAREHOUSE, FOUNDED_AT))
                .isInstanceOf(InvalidTownPlotPositionException.class);
        assertThat(town.resources().woodAmount()).isEqualTo(500);
        assertThat(town.plots()).isEqualTo(TownPlots.standard());
    }

    @Test
    void startingConstructionOnAPlotThatIsNotFreeLeavesResourcesIntact() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThatThrownBy(() -> town.startingConstruction(5, BuildingType.WAREHOUSE, FOUNDED_AT))
                .isInstanceOf(TownPlotNotFreeException.class);
        assertThat(town.resources().woodAmount()).isEqualTo(500);
        assertThat(town.resources().luxuryAmount()).isEqualTo(100);
    }

    @Test
    void startingConstructionOfTheWrongKindLeavesResourcesIntact() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThatThrownBy(() -> town.startingConstruction(12, BuildingType.WAREHOUSE, FOUNDED_AT))
                .isInstanceOf(MismatchedBuildingTypeException.class);
        assertThat(town.resources().woodAmount()).isEqualTo(500);
        assertThat(town.resources().luxuryAmount()).isEqualTo(100);
    }

    @Test
    void startingConstructionOfTheWrongKindIsRejectedBeforeSpending() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT).spend(480, 0);

        assertThatThrownBy(() -> town.startingConstruction(2, BuildingType.WALL, FOUNDED_AT))
                .isInstanceOf(MismatchedBuildingTypeException.class);
    }

    @Test
    void startingConstructionOnALockedPlotIsRejectedBeforeSpending() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT).spend(450, 0);

        assertThatThrownBy(() -> town.startingConstruction(5, BuildingType.BARRACKS, FOUNDED_AT))
                .isInstanceOf(TownPlotNotFreeException.class);
    }

    @Test
    void startingConstructionWithoutEnoughResourcesLeavesThePlotFree() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT).spend(450, 0);

        assertThatThrownBy(() -> town.startingConstruction(2, BuildingType.BARRACKS, FOUNDED_AT))
                .isInstanceOf(NotEnoughResourcesException.class);
        assertThat(town.plot(2).state(town.townHallLevel())).isEqualTo(TownPlotState.FREE);
    }

    @Test
    void advancedToCompletesEachOverdueConstructionIndependently() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);
        Town underConstruction = town.startingConstruction(2, BuildingType.WAREHOUSE, FOUNDED_AT)
                .startingConstruction(3, BuildingType.TAVERN, FOUNDED_AT);

        Town advanced = underConstruction.advancedTo(FOUNDED_AT.plus(Duration.ofMinutes(7)));

        assertThat(advanced.plot(2).building()).contains(new Building(BuildingType.WAREHOUSE, 1));
        assertThat(advanced.plot(3).construction())
                .contains(Construction.startingAt(BuildingType.TAVERN, FOUNDED_AT));
    }

    @Test
    void nextFinishAtIsEmptyWithoutAnyConstruction() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThat(town.nextFinishAt()).isEmpty();
    }

    @Test
    void nextFinishAtIsTheFinishesAtOfItsOnlyConstruction() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT).startingConstruction(2, BuildingType.WAREHOUSE, FOUNDED_AT);

        assertThat(town.nextFinishAt()).contains(FOUNDED_AT.plus(BuildingType.WAREHOUSE.buildTime()));
    }

    @Test
    void nextFinishAtIsTheNearestOfSeveralConstructions() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT)
                .startingConstruction(2, BuildingType.WAREHOUSE, FOUNDED_AT)
                .startingConstruction(3, BuildingType.TAVERN, FOUNDED_AT);

        assertThat(town.nextFinishAt()).contains(FOUNDED_AT.plus(BuildingType.WAREHOUSE.buildTime()));
    }

    @Test
    void aTownWithoutAnOccupiedTownHallIsRejected() {
        List<TownPlot> withoutAnOccupiedTownHall = new ArrayList<>(TownPlots.standard());
        withoutAnOccupiedTownHall.set(0, new TownPlot(1, TownPlotKind.TOWN_HALL, 1));

        assertThatThrownBy(() -> new Town(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                withoutAnOccupiedTownHall, TownResources.starting(LuxuryResource.WINE, FOUNDED_AT)))
                .isInstanceOf(MissingTownHallException.class);
    }
}
