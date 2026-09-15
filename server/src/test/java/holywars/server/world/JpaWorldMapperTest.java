package holywars.server.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.world.Coordinate;
import holywars.world.InvalidIslandPlotCountException;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.IslandPlot;
import holywars.world.LuxuryResource;
import holywars.world.World;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class JpaWorldMapperTest {

    private final JpaWorldMapper worldMapper = new JpaWorldMapper();

    @Test
    void mapsNoIslandEntitiesToAWorldWithNoIslands() {
        World world = worldMapper.toDomain(List.of());

        assertThat(world.islands()).isEmpty();
    }

    @Test
    void mapsASingleIslandEntityToItsMatchingDomainIsland() {
        JpaIsland naxos = anIslandEntityWithFreePlots(3, 5, 8, "Naxos", "WINE");

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

    @Test
    void mapsSeveralIslandEntitiesPreservingEachOnesIdentity() {
        JpaIsland naxos = anIslandEntityWithFreePlots(1, 0, 0, "Naxos", "WINE");
        JpaIsland ikaria = anIslandEntityWithFreePlots(2, 1, 1, "Ikaria", "MARBLE");

        World world = worldMapper.toDomain(List.of(naxos, ikaria));

        assertThat(world.islands()).hasSize(2);
        Island mappedNaxos = world.island(new IslandId(1));
        Island mappedIkaria = world.island(new IslandId(2));
        assertThat(mappedNaxos.name()).isEqualTo("Naxos");
        assertThat(mappedIkaria.name()).isEqualTo("Ikaria");
        assertThat(mappedNaxos.plots()).hasSize(16);
        assertThat(mappedIkaria.plots()).hasSize(16);
        assertThat(mappedNaxos.plots()).allMatch(IslandPlot::isFree);
        assertThat(mappedIkaria.plots()).allMatch(IslandPlot::isFree);
    }

    @Test
    void mapsAnOccupiedPlotWithItsOccupantTownId() {
        JpaIsland naxos = new JpaIsland(1, 0, 0, "Naxos", "WINE");
        naxos.addPlot(new JpaIslandPlot(naxos, 1, 7L));
        for (int number = 2; number <= 16; number++) {
            naxos.addPlot(new JpaIslandPlot(naxos, number, null));
        }

        World world = worldMapper.toDomain(List.of(naxos));

        Island island = world.island(new IslandId(1));
        IslandPlot firstPlot = island.plots().get(0);
        IslandPlot lastPlot = island.plots().get(15);
        assertThat(firstPlot.isFree()).isFalse();
        assertThat(firstPlot.occupant()).hasValue(7L);
        assertThat(lastPlot.isFree()).isTrue();
    }

    @Test
    void mapsADomainIslandToItsPersistableIslandAndPlotEntities() {
        Island naxos = Island.withFreePlots(new IslandId(4), new Coordinate(2, 6), "Naxos", LuxuryResource.CRYSTAL);
        naxos.firstFreePlot().occupy(9L);

        JpaIsland islandEntity = worldMapper.toEntity(naxos);

        assertThat(islandEntity.id()).isEqualTo(4L);
        assertThat(islandEntity.x()).isEqualTo(2);
        assertThat(islandEntity.y()).isEqualTo(6);
        assertThat(islandEntity.name()).isEqualTo("Naxos");
        assertThat(islandEntity.luxuryResource()).isEqualTo("CRYSTAL");
        assertThat(islandEntity.plots()).hasSize(16);
        JpaIslandPlot firstPlotEntity = islandEntity.plots().stream()
                .filter(plot -> plot.number() == 1)
                .findFirst()
                .orElseThrow();
        assertThat(firstPlotEntity.island()).isSameAs(islandEntity);
        assertThat(firstPlotEntity.occupantTownId()).isEqualTo(9L);
        JpaIslandPlot secondPlotEntity = islandEntity.plots().stream()
                .filter(plot -> plot.number() == 2)
                .findFirst()
                .orElseThrow();
        assertThat(secondPlotEntity.occupantTownId()).isNull();
    }

    @Test
    void refusesToRebuildAnIslandEntityWithoutSixteenPlots() {
        JpaIsland naxos = new JpaIsland(1, 0, 0, "Naxos", "WINE");
        for (int number = 1; number <= 15; number++) {
            naxos.addPlot(new JpaIslandPlot(naxos, number, null));
        }

        assertThatThrownBy(() -> worldMapper.toDomain(List.of(naxos)))
                .isInstanceOf(InvalidIslandPlotCountException.class);
    }

    private JpaIsland anIslandEntityWithFreePlots(long id, int x, int y, String name, String luxuryResource) {
        JpaIsland islandEntity = new JpaIsland(id, x, y, name, luxuryResource);
        for (int number = 1; number <= 16; number++) {
            islandEntity.addPlot(new JpaIslandPlot(islandEntity, number, null));
        }
        return islandEntity;
    }
}
