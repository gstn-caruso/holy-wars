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
        Town firstTown = new Town(firstTownId, "Sparta", firstIslandId);
        TownId secondTownId = new TownId(20L);
        Town secondTown = new Town(secondTownId, "Corinth", secondIslandId);

        InMemoryTowns towns = new InMemoryTowns(Map.of(firstTownId, firstTown, secondTownId, secondTown));
        InMemoryIslands islands = new InMemoryIslands(Map.of(firstIslandId, firstIsland, secondIslandId, secondIsland));
        ViewTownDetail viewTownDetail = new ViewTownDetail(towns, islands);

        ViewTownDetailResponse response = viewTownDetail.run(new ViewTownDetailRequest(secondTownId));

        assertThat(response).isEqualTo(new ViewTownDetailResponse(
                "Corinth", "Thira", new Coordinates(20, 45), Resource.MARBLE));
    }
}
