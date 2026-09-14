package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class IslandTest {

    @Test
    void aNewIslandHasSixteenFreePlotsNumberedOneToSixteenAndTheFirstFreeIsOne() {
        Island island = Island.withFreePlots(new IslandId(1), new Coordinate(3, 4), "Naxos", LuxuryResource.WINE);

        assertThat(island.plots()).hasSize(16);
        assertThat(island.plots()).allMatch(IslandPlot::isFree);
        assertThat(island.plots().stream().map(IslandPlot::number))
                .containsExactlyElementsOf(IntStream.rangeClosed(1, 16).boxed().toList());
        assertThat(island.firstFreePlot().number()).isEqualTo(1);
    }

    @Test
    void anIslandListsItsOccupiedPlotsInPlotOrder() {
        Island island = Island.withFreePlots(new IslandId(1), new Coordinate(3, 4), "Naxos", LuxuryResource.WINE);
        island.plots().get(5).occupy(10L);
        island.plots().get(2).occupy(20L);

        assertThat(island.occupiedPlots()).extracting(IslandPlot::number).containsExactly(3, 6);
    }

    @Test
    void anIslandCannotBeCreatedWithoutExactlySixteenPlots() {
        List<IslandPlot> fifteenPlots = new ArrayList<>();
        for (int number = 1; number <= 15; number++) {
            fifteenPlots.add(new IslandPlot(number));
        }

        assertThatThrownBy(() -> new Island(
                new IslandId(1),
                new Coordinate(3, 4),
                "Naxos",
                LuxuryResource.WINE,
                fifteenPlots))
                .isInstanceOf(InvalidIslandPlotCountException.class);
    }
}
