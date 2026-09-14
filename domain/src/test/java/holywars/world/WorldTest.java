package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class WorldTest {

    @Test
    void aWorldResolvesAnExistingIslandById() {
        Island naxos = anIsland(1, "Naxos");
        Island ikaria = anIsland(2, "Ikaria");
        World world = new World(List.of(naxos, ikaria));

        assertThat(world.island(new IslandId(2))).isSameAs(ikaria);
    }

    @Test
    void aWorldRejectsAnUnknownIslandId() {
        World world = new World(List.of(anIsland(1, "Naxos")));

        assertThatThrownBy(() -> world.island(new IslandId(99)))
                .isInstanceOf(UnknownIslandException.class);
    }

    @Test
    void aWorldChoosesARandomIslandUsingTheGivenRandom() {
        List<Island> islands = List.of(anIsland(1, "Naxos"), anIsland(2, "Ikaria"), anIsland(3, "Milos"));
        World world = new World(islands);
        long seed = 42L;
        int expectedIndex = new Random(seed).nextInt(islands.size());

        assertThat(world.randomIsland(new Random(seed))).isSameAs(islands.get(expectedIndex));
    }

    private Island anIsland(long id, String name) {
        return Island.withFreePlots(new IslandId(id), new Coordinate(0, 0), name, LuxuryResource.WINE);
    }
}
