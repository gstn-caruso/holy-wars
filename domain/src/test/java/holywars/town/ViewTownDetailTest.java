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

class ViewTownDetailTest {

    @Test
    void returnsTheRequestedTownsNameAndItsIslandsNameCoordinatesAndSpecialResource() {
        IslandId firstIslandId = new IslandId(1L);
        Island firstIsland = new Island(
                firstIslandId, "Argos", new Coordinates(3, 7), SpecialResource.WINE);
        IslandId secondIslandId = new IslandId(2L);
        Island secondIsland = new Island(
                secondIslandId, "Thira", new Coordinates(20, 45), SpecialResource.MARBLE);

        TownId firstTownId = new TownId(10L);
        Town firstTown = new Town(firstTownId, "Sparta", firstIslandId, ResourceStock.empty());
        TownId secondTownId = new TownId(20L);
        Town secondTown = new Town(secondTownId, "Corinth", secondIslandId, ResourceStock.empty());

        InMemoryTowns towns = new InMemoryTowns(Map.of(firstTownId, firstTown, secondTownId, secondTown));
        InMemoryIslands islands = new InMemoryIslands(Map.of(firstIslandId, firstIsland, secondIslandId, secondIsland));
        ViewTownDetail viewTownDetail = new ViewTownDetail(towns, islands);

        ViewTownDetailResponse response = viewTownDetail.run(new ViewTownDetailRequest(secondTownId));

        assertThat(response).isEqualTo(new ViewTownDetailResponse(
                "Corinth", "Thira", new Coordinates(20, 45), Resource.MARBLE, zeroStock()));
    }

    @Test
    void reportsEveryResourceAtZeroWhenTheTownHasNothingStocked() {
        IslandId islandId = new IslandId(1L);
        Island island = new Island(islandId, "Argos", new Coordinates(3, 7), SpecialResource.WINE);
        TownId townId = new TownId(10L);
        Town town = new Town(townId, "Sparta", islandId, ResourceStock.empty());

        InMemoryTowns towns = new InMemoryTowns(Map.of(townId, town));
        InMemoryIslands islands = new InMemoryIslands(Map.of(islandId, island));
        ViewTownDetail viewTownDetail = new ViewTownDetail(towns, islands);

        ViewTownDetailResponse response = viewTownDetail.run(new ViewTownDetailRequest(townId));

        assertThat(response.resourceStock()).isEqualTo(zeroStock());
    }

    @Test
    void reportsTheStockedAmountOfEachResourceKeepingTheRestAtZero() {
        IslandId islandId = new IslandId(1L);
        Island island = new Island(islandId, "Argos", new Coordinates(3, 7), SpecialResource.WINE);
        TownId townId = new TownId(10L);
        ResourceStock stock = new ResourceStock(Map.of(
                Resource.WINE, new ResourceAmount(120L),
                Resource.MARBLE, new ResourceAmount(45L)));
        Town town = new Town(townId, "Sparta", islandId, stock);

        InMemoryTowns towns = new InMemoryTowns(Map.of(townId, town));
        InMemoryIslands islands = new InMemoryIslands(Map.of(islandId, island));
        ViewTownDetail viewTownDetail = new ViewTownDetail(towns, islands);

        ViewTownDetailResponse response = viewTownDetail.run(new ViewTownDetailRequest(townId));

        assertThat(response.resourceStock()).isEqualTo(Map.of(
                Resource.WOOD, 0L,
                Resource.WINE, 120L,
                Resource.MARBLE, 45L,
                Resource.CRYSTAL, 0L,
                Resource.SULFUR, 0L));
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
