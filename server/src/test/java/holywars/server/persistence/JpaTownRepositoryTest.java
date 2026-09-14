package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.Building;
import holywars.town.BuildingSlot;
import holywars.town.BuildingSlots;
import holywars.town.BuildingType;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.town.TownResources;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JpaTownRepositoryTest {

    private static final Instant FOUNDED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savedTownIsFoundBackEqualToTheOriginal() {
        Town town = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT);

        townRepository.save(town);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.find(new TownId(1))).contains(town);
    }

    @Test
    void savedTownWithAdvancedResourcesIsFoundBackEqualToTheOriginal() {
        Town town = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT)
                .advancedTo(FOUNDED_AT.plus(Duration.ofHours(1)));

        townRepository.save(town);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.find(new TownId(1))).contains(town);
    }

    @Test
    void savedTownWithABuildingInALandSlotIsFoundBackWithThatBuilding() {
        List<BuildingSlot> slots = withBuildingAt(BuildingSlots.standard(1), 5, new Building(BuildingType.ACADEMY, 2));
        Town town = new Town(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), slots,
                TownResources.initial(LuxuryResource.WINE, FOUNDED_AT));

        townRepository.save(town);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.find(new TownId(1))).contains(town);
    }

    @Test
    void findIsEmptyForAnUnknownTown() {
        assertThat(townRepository.find(new TownId(99))).isEmpty();
    }

    @Test
    void findByIslandReturnsTheTownsFoundedOnThatIsland() {
        Town onIslandOne = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT);
        Town alsoOnIslandOne = Town.founded(new TownId(2), "Atenas", new PlayerId(2), new PlotLocation(new IslandId(1), 5), LuxuryResource.WINE, FOUNDED_AT);
        Town onIslandTwo = Town.founded(new TownId(3), "Tebas", new PlayerId(3), new PlotLocation(new IslandId(2), 1), LuxuryResource.WINE, FOUNDED_AT);
        townRepository.save(onIslandOne);
        townRepository.save(alsoOnIslandOne);
        townRepository.save(onIslandTwo);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.findByIsland(new IslandId(1))).containsExactlyInAnyOrder(onIslandOne, alsoOnIslandOne);
    }

    @Test
    void findByIslandIsEmptyWhenThereAreNoTownsThere() {
        assertThat(townRepository.findByIsland(new IslandId(1))).isEmpty();
    }

    @Test
    void findByOwnerReturnsTheTownsFoundedByThatPlayer() {
        Town first = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT);
        Town second = Town.founded(new TownId(2), "Corinto", new PlayerId(1), new PlotLocation(new IslandId(2), 4), LuxuryResource.WINE, FOUNDED_AT);
        Town other = Town.founded(new TownId(3), "Tebas", new PlayerId(2), new PlotLocation(new IslandId(3), 1), LuxuryResource.WINE, FOUNDED_AT);
        townRepository.save(first);
        townRepository.save(second);
        townRepository.save(other);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.findByOwner(new PlayerId(1))).containsExactlyInAnyOrder(first, second);
    }

    @Test
    void findByOwnerIsEmptyWhenThePlayerHasNoTowns() {
        assertThat(townRepository.findByOwner(new PlayerId(1))).isEmpty();
    }

    @Test
    void findByOwnerReturnsTownsOrderedById() {
        Town higherId = Town.founded(new TownId(2), "Corinto", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT);
        Town lowerId = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(2), 4), LuxuryResource.WINE, FOUNDED_AT);
        townRepository.save(higherId);
        townRepository.save(lowerId);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.findByOwner(new PlayerId(1))).containsExactly(lowerId, higherId);
    }

    @Test
    void savingATownAgainReplacesItsSlots() {
        Town town = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT);
        townRepository.save(town);
        entityManager.flush();
        entityManager.clear();

        List<BuildingSlot> slotsWithAnAcademy = withBuildingAt(BuildingSlots.standard(1), 5, new Building(BuildingType.ACADEMY, 1));
        Town townWithAnAcademy = new Town(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), slotsWithAnAcademy,
                TownResources.initial(LuxuryResource.WINE, FOUNDED_AT));
        townRepository.save(townWithAnAcademy);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.find(new TownId(1))).contains(townWithAnAcademy);
    }

    @Test
    void savingATownAgainPersistsItsAdvancedResources() {
        Town town = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT);
        townRepository.save(town);
        entityManager.flush();
        entityManager.clear();

        Town advancedTown = town.advancedTo(FOUNDED_AT.plus(Duration.ofHours(1)));
        townRepository.save(advancedTown);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.find(new TownId(1))).contains(advancedTown);
    }

    @Test
    void savedTownWithASlotUnderConstructionIsFoundBackEqualToTheOriginal() {
        Town founded = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT);
        Town town = founded.startingConstruction(2, BuildingType.ACADEMY, FOUNDED_AT);

        townRepository.save(town);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.find(new TownId(1))).contains(town);
    }

    @Test
    void savedTownWithAnOccupiedWallSlotIsFoundBackWithThatWall() {
        List<BuildingSlot> slots = withBuildingAt(BuildingSlots.standard(1), 12, new Building(BuildingType.WALL, 1));
        Town town = new Town(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), slots,
                TownResources.initial(LuxuryResource.WINE, FOUNDED_AT));

        townRepository.save(town);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.find(new TownId(1))).contains(town);
    }

    private static List<BuildingSlot> withBuildingAt(List<BuildingSlot> slots, int position, Building building) {
        return slots.stream()
                .map(slot -> slot.position() == position
                        ? new BuildingSlot(slot.position(), slot.kind(), slot.requiredTownHallLevel(), Optional.of(building))
                        : slot)
                .toList();
    }
}
