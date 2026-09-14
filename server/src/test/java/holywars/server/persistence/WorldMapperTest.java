package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.IslandPlot;
import holywars.world.LuxuryResource;
import holywars.world.World;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class WorldMapperTest {

    private final WorldMapper worldMapper = new WorldMapper();

    @Test
    void mapsNoIslandEntitiesToAWorldWithNoIslands() {
        World world = worldMapper.toDomain(List.of());

        assertThat(world.islands()).isEmpty();
    }

    @Test
    void mapsASingleIslandEntityToItsMatchingDomainIsland() {
        IslandEntity naxos = anIslandEntityWithFreePlots(3, 5, 8, "Naxos", "WINE");

        World world = worldMapper.toDomain(List.of(naxos));

        assertThat(world.islands()).hasSize(1);
        Island island = world.island(new IslandId(3));
        assertThat(island.coordinate()).isEqualTo(new Coordinate(5, 8));
        assertThat(island.name()).isEqualTo("Naxos");
        assertThat(island.luxuryResource()).isEqualTo(LuxuryResource.WINE);
        assertThat(island.plots()).hasSize(16);
        assertThat(island.plots()).allMatch(IslandPlot::isFree);
        assertThat(island.plots().stream().map(IslandPlot::number))
                .containsExactlyElementsOf(IntStream.rangeClosed(1, 16).boxed().toList());
    }

    private IslandEntity anIslandEntityWithFreePlots(long id, int x, int y, String name, String luxuryResource) {
        IslandEntity islandEntity = new IslandEntity(id, x, y, name, luxuryResource);
        for (int number = 1; number <= 16; number++) {
            islandEntity.addPlot(new IslandPlotEntity(islandEntity, number, null));
        }
        return islandEntity;
    }
}
