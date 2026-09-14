package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class IslandTest {

    @Test
    void aNewIslandHasSixteenFreePlotsNumberedOneToSixteenAndTheFirstFreeIsOne() {
        Island island = new Island(
                new IslandId(1),
                new Coordinate(3, 4),
                "Naxos",
                LuxuryResource.WINE,
                sixteenFreePlots());

        assertThat(island.plots()).hasSize(16);
        assertThat(island.plots()).allMatch(IslandPlot::isFree);
        assertThat(island.plots().stream().map(IslandPlot::number))
                .containsExactlyElementsOf(IntStream.rangeClosed(1, 16).boxed().toList());
        assertThat(island.firstFreePlot().number()).isEqualTo(1);
    }

    private List<IslandPlot> sixteenFreePlots() {
        List<IslandPlot> plots = new ArrayList<>();
        for (int number = 1; number <= 16; number++) {
            plots.add(new IslandPlot(number));
        }
        return plots;
    }
}
