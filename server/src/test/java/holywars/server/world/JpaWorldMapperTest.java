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

    private final JpaWorldMapper jpaWorldMapper = new JpaWorldMapper();

    @Test
    void mapsNoIslandEntitiesToAWorldWithNoIslands() {
        World world = jpaWorldMapper.toDomain(List.of());

        assertThat(world.islands()).isEmpty();
    }

    @Test
    void mapsASingleIslandEntityToItsMatchingDomainIsland() {
        JpaIsland naxos = anIslandEntityWithFreePlots(3, 5, 8, "Naxos", "WINE");

        World world = jpaWorldMapper.toDomain(List.of(naxos));

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

        World world = jpaWorldMapper.toDomain(List.of(naxos, ikaria));

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

        World world = jpaWorldMapper.toDomain(List.of(naxos));

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

        JpaIsland jpaIsland = jpaWorldMapper.toEntity(naxos);

        assertThat(jpaIsland.id()).isEqualTo(4L);
        assertThat(jpaIsland.x()).isEqualTo(2);
        assertThat(jpaIsland.y()).isEqualTo(6);
        assertThat(jpaIsland.name()).isEqualTo("Naxos");
        assertThat(jpaIsland.luxuryResource()).isEqualTo("CRYSTAL");
        assertThat(jpaIsland.plots()).hasSize(16);
        JpaIslandPlot firstJpaIslandPlot = jpaIsland.plots().stream()
                .filter(plot -> plot.number() == 1)
                .findFirst()
                .orElseThrow();
        assertThat(firstJpaIslandPlot.island()).isSameAs(jpaIsland);
        assertThat(firstJpaIslandPlot.occupantTownId()).isEqualTo(9L);
        JpaIslandPlot secondJpaIslandPlot = jpaIsland.plots().stream()
                .filter(plot -> plot.number() == 2)
                .findFirst()
                .orElseThrow();
        assertThat(secondJpaIslandPlot.occupantTownId()).isNull();
    }

    @Test
    void refusesToRebuildAnIslandEntityWithoutSixteenPlots() {
        JpaIsland naxos = new JpaIsland(1, 0, 0, "Naxos", "WINE");
        for (int number = 1; number <= 15; number++) {
            naxos.addPlot(new JpaIslandPlot(naxos, number, null));
        }

        assertThatThrownBy(() -> jpaWorldMapper.toDomain(List.of(naxos)))
                .isInstanceOf(InvalidIslandPlotCountException.class);
    }

    private JpaIsland anIslandEntityWithFreePlots(long id, int x, int y, String name, String luxuryResource) {
        JpaIsland jpaIsland = new JpaIsland(id, x, y, name, luxuryResource);
        for (int number = 1; number <= 16; number++) {
            jpaIsland.addPlot(new JpaIslandPlot(jpaIsland, number, null));
        }
        return jpaIsland;
    }
}
