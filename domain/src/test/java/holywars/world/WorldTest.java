package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.town.PlotLocation;
import holywars.town.TownId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class WorldTest {

    private static final GridSize GRID = new GridSize(10, 10);

    @Test
    void worldFindsAnExistingIslandById() {
        Island naxos = islandNamed(1, "Naxos");
        Island ikaria = islandNamed(2, "Ikaria");
        World world = new World(GRID, List.of(naxos, ikaria));

        assertThat(world.island(new IslandId(2))).contains(ikaria);
    }

    @Test
    void worldReturnsEmptyForAnUnknownId() {
        World world = new World(GRID, List.of(islandNamed(1, "Naxos")));

        assertThat(world.island(new IslandId(99))).isEqualTo(Optional.empty());
    }

    @Test
    void worldListsAllIslands() {
        Island naxos = islandNamed(1, "Naxos");
        Island ikaria = islandNamed(2, "Ikaria");
        World world = new World(GRID, List.of(naxos, ikaria));

        assertThat(world.islands()).containsExactly(naxos, ikaria);
    }

    @Test
    void worldLocatesTheIslandAtACoordinate() {
        Island naxos = islandNamed(1, "Naxos");
        World world = new World(GRID, List.of(naxos));

        assertThat(world.islandAt(naxos.coordinate())).contains(naxos);
    }

    @Test
    void worldFindsNoIslandAtAnEmptyCoordinate() {
        Island naxos = islandNamed(1, "Naxos");
        World world = new World(GRID, List.of(naxos));

        assertThat(world.islandAt(new Coordinate(9, 9))).isEqualTo(Optional.empty());
    }

    @Test
    void foundingACityUpdatesOnlyTheTargetIsland() {
        Island naxos = islandNamed(1, "Naxos");
        Island ikaria = islandNamed(2, "Ikaria");
        World world = new World(GRID, List.of(naxos, ikaria));
        TownId townId = new TownId(1);

        World updated = world.withCityFounded(new PlotLocation(new IslandId(2), 5), townId);

        assertThat(updated.island(new IslandId(1))).contains(naxos);
        Island updatedIkaria = updated.island(new IslandId(2)).orElseThrow();
        assertThat(updatedIkaria.plots().stream().filter(plot -> plot.number() == 5).findFirst().orElseThrow().town())
                .isEqualTo(Optional.of(townId));
    }

    @Test
    void foundingACityOnAnUnknownIslandIsRejected() {
        World world = new World(GRID, List.of(islandNamed(1, "Naxos")));

        assertThatThrownBy(() -> world.withCityFounded(new PlotLocation(new IslandId(99), 1), new TownId(1)))
                .isInstanceOf(UnknownIslandException.class);
    }

    private Island islandNamed(int id, String name) {
        return Island.withFreePlots(new IslandId(id), new Coordinate(id, id), name, LuxuryResource.WINE);
    }
}
