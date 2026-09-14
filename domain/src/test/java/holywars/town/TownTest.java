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
    void foundedTownHasTheStandardBuildingSlotLayoutAndTownHallLevelOne() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThat(town.buildingSlots()).isEqualTo(BuildingSlots.standard());
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
    void slotReturnsTheBuildingSlotAtThatPosition() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThat(town.slot(1)).isEqualTo(BuildingSlots.standard().get(0));
    }

    @Test
    void slotAtANonExistentPositionIsRejected() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThatThrownBy(() -> town.slot(15))
                .isInstanceOf(InvalidBuildingSlotPositionException.class);
    }

    @Test
    void aTownWithoutExactlyItsFourteenDistinctPositionsIsRejected() {
        List<BuildingSlot> missingOnePosition = new ArrayList<>(BuildingSlots.standard());
        missingOnePosition.remove(missingOnePosition.size() - 1);

        assertThatThrownBy(() -> new Town(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                missingOnePosition, TownResources.starting(LuxuryResource.WINE, FOUNDED_AT)))
                .isInstanceOf(InvalidBuildingSlotCountException.class);
    }

    @Test
    void aTownWithARepeatedBuildingSlotPositionIsRejected() {
        List<BuildingSlot> repeatedPosition = new ArrayList<>(BuildingSlots.standard());
        repeatedPosition.set(13, new BuildingSlot(13, BuildingSlotKind.COAST, 1));

        assertThatThrownBy(() -> new Town(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                repeatedPosition, TownResources.starting(LuxuryResource.WINE, FOUNDED_AT)))
                .isInstanceOf(InvalidBuildingSlotCountException.class)
                .hasMessage("A town must have exactly 14 distinct building slot positions, got 13");
    }

    @Test
    void startingConstructionSpendsResourcesAndLeavesTheConstruction() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        Town underConstruction = town.startingConstruction(2, BuildingType.WAREHOUSE, FOUNDED_AT);

        assertThat(underConstruction.resources().woodAmount()).isEqualTo(460);
        assertThat(underConstruction.resources().luxuryAmount()).isEqualTo(100);
        assertThat(underConstruction.slot(2).construction())
                .contains(Construction.startingAt(BuildingType.WAREHOUSE, FOUNDED_AT));
    }

    @Test
    void startingConstructionAtANonExistentPositionChangesNothing() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThatThrownBy(() -> town.startingConstruction(15, BuildingType.WAREHOUSE, FOUNDED_AT))
                .isInstanceOf(InvalidBuildingSlotPositionException.class);
        assertThat(town.resources().woodAmount()).isEqualTo(500);
        assertThat(town.buildingSlots()).isEqualTo(BuildingSlots.standard());
    }

    @Test
    void startingConstructionOnASlotThatIsNotFreeLeavesResourcesIntact() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT);

        assertThatThrownBy(() -> town.startingConstruction(5, BuildingType.WAREHOUSE, FOUNDED_AT))
                .isInstanceOf(SlotNotFreeException.class);
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
    void startingConstructionWithoutEnoughResourcesLeavesTheSlotFree() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, FOUNDED_AT).spend(450, 0);

        assertThatThrownBy(() -> town.startingConstruction(2, BuildingType.BARRACKS, FOUNDED_AT))
                .isInstanceOf(NotEnoughResourcesException.class);
        assertThat(town.slot(2).state(town.townHallLevel())).isEqualTo(BuildingSlotState.FREE);
    }

    @Test
    void aTownWithoutAnOccupiedTownHallIsRejected() {
        List<BuildingSlot> withoutAnOccupiedTownHall = new ArrayList<>(BuildingSlots.standard());
        withoutAnOccupiedTownHall.set(0, new BuildingSlot(1, BuildingSlotKind.TOWN_HALL, 1));

        assertThatThrownBy(() -> new Town(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                withoutAnOccupiedTownHall, TownResources.starting(LuxuryResource.WINE, FOUNDED_AT)))
                .isInstanceOf(MissingTownHallException.class);
    }
}
