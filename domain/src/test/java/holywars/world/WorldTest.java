package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class WorldTest {

    @Test
    void aWorldResolvesAnExistingIslandById() {
        Island naxos = anIsland(1, "Naxos");
        Island ikaria = anIsland(2, "Ikaria");
        World world = new World(List.of(naxos, ikaria));

        assertThat(world.island(new IslandId(2))).isSameAs(ikaria);
    }

    private Island anIsland(long id, String name) {
        return new Island(new IslandId(id), new Coordinate(0, 0), name, LuxuryResource.WINE, sixteenFreePlots());
    }

    private List<IslandPlot> sixteenFreePlots() {
        List<IslandPlot> plots = new ArrayList<>();
        for (int number = 1; number <= 16; number++) {
            plots.add(new IslandPlot(number));
        }
        return plots;
    }
}
