package holywars.town;

import holywars.world.Coordinates;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.InMemoryIslands;
import holywars.world.Resource;
import holywars.world.SpecialResource;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ViewTownDetailTest {

    @Test
    void returnsTheRequestedTownsNameAndItsIslandsNameCoordinatesAndSpecialResource() {
        IslandId firstIslandId = new IslandId(1L);
        Island firstIsland = argosIsland(firstIslandId);
        IslandId secondIslandId = new IslandId(2L);
        Island secondIsland = new Island(
                secondIslandId, "Thira", new Coordinates(20, 45), SpecialResource.MARBLE);

        TownId firstTownId = new TownId(10L);
        Town firstTown = TownBuilder.aTown().withId(firstTownId).withName("Sparta").onIsland(firstIslandId).build();
        TownId secondTownId = new TownId(20L);
        Town secondTown = TownBuilder.aTown().withId(secondTownId).withName("Corinth").onIsland(secondIslandId).build();

        ViewTownDetail viewTownDetail = viewTownDetailFor(
                Map.of(firstTownId, firstTown, secondTownId, secondTown),
                Map.of(firstIslandId, firstIsland, secondIslandId, secondIsland));

        ViewTownDetailResponse response = viewTownDetail.run(new ViewTownDetailRequest(secondTownId));

        assertThat(response).isEqualTo(new ViewTownDetailResponse(
                "Corinth", "Thira", new Coordinates(20, 45), Resource.MARBLE, zeroStock()));
    }

    @Test
    void reportsEveryResourceAtZeroWhenTheTownHasNothingStocked() {
        IslandId islandId = new IslandId(1L);
        Island island = argosIsland(islandId);
        TownId townId = new TownId(10L);
        Town town = TownBuilder.aTown().withId(townId).withName("Sparta").onIsland(islandId).build();

        ViewTownDetail viewTownDetail = viewTownDetailFor(Map.of(townId, town), Map.of(islandId, island));

        ViewTownDetailResponse response = viewTownDetail.run(new ViewTownDetailRequest(townId));

        assertThat(response.resourceStock()).isEqualTo(zeroStock());
    }

    @Test
    void reportsTheStockedAmountOfEachResourceKeepingTheRestAtZero() {
        IslandId islandId = new IslandId(1L);
        Island island = argosIsland(islandId);
        TownId townId = new TownId(10L);
        ResourceStock stock = new ResourceStock(Map.of(
                Resource.WINE, new ResourceAmount(120L),
                Resource.MARBLE, new ResourceAmount(45L)));
        Town town = TownBuilder.aTown().withId(townId).withName("Sparta").onIsland(islandId).stocking(stock).build();

        ViewTownDetail viewTownDetail = viewTownDetailFor(Map.of(townId, town), Map.of(islandId, island));

        ViewTownDetailResponse response = viewTownDetail.run(new ViewTownDetailRequest(townId));

        assertThat(response.resourceStock()).isEqualTo(Map.of(
                Resource.WOOD, 0L,
                Resource.WINE, 120L,
                Resource.MARBLE, 45L,
                Resource.CRYSTAL, 0L,
                Resource.SULFUR, 0L));
    }

    @Test
    void failsWithUnknownTownExceptionWhenTheTownDoesNotExist() {
        ViewTownDetail viewTownDetail = viewTownDetailFor(Map.of(), Map.of());
        TownId unknownTownId = new TownId(999L);

        assertThatThrownBy(() -> viewTownDetail.run(new ViewTownDetailRequest(unknownTownId)))
                .isInstanceOf(UnknownTownException.class)
                .hasMessageContaining(unknownTownId.toString());
    }

    private static Island argosIsland(IslandId id) {
        return new Island(id, "Argos", new Coordinates(3, 7), SpecialResource.WINE);
    }

    private static ViewTownDetail viewTownDetailFor(Map<TownId, Town> towns, Map<IslandId, Island> islands) {
        return new ViewTownDetail(new InMemoryTowns(towns), new InMemoryIslands(islands));
    }

    private static Map<Resource, Long> zeroStock() {
        return Map.of(
                Resource.WOOD, 0L,
                Resource.WINE, 0L,
                Resource.MARBLE, 0L,
                Resource.CRYSTAL, 0L,
                Resource.SULFUR, 0L);
    }
}
