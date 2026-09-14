package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class WorldTest {

    @Test
    void worldFindsAnExistingIslandById() {
        Island naxos = islandNamed(1, "Naxos");
        Island ikaria = islandNamed(2, "Ikaria");
        World world = new World(List.of(naxos, ikaria));

        assertThat(world.island(new IslandId(2))).contains(ikaria);
    }

    @Test
    void worldReturnsEmptyForAnUnknownId() {
        World world = new World(List.of(islandNamed(1, "Naxos")));

        assertThat(world.island(new IslandId(99))).isEqualTo(Optional.empty());
    }

    @Test
    void worldListsAllIslands() {
        Island naxos = islandNamed(1, "Naxos");
        Island ikaria = islandNamed(2, "Ikaria");
        World world = new World(List.of(naxos, ikaria));

        assertThat(world.islands()).containsExactly(naxos, ikaria);
    }

    private Island islandNamed(int id, String name) {
        return Island.withFreePlots(new IslandId(id), new Coordinate(id, id), name, LuxuryResource.WINE);
    }
}
