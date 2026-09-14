package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.town.TownId;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class IslandTest {

    @Test
    void islandExposesIdCoordinateNameResourceAndPlots() {
        IslandId id = new IslandId(1);
        Coordinate coordinate = new Coordinate(3, 4);

        Island island = Island.withFreePlots(id, coordinate, "Naxos", LuxuryResource.WINE);

        assertThat(island.id()).isEqualTo(id);
        assertThat(island.coordinate()).isEqualTo(coordinate);
        assertThat(island.name()).isEqualTo("Naxos");
        assertThat(island.resource()).isEqualTo(LuxuryResource.WINE);
        assertThat(island.plots()).hasSize(16);
        assertThat(island.plots()).allSatisfy(plot -> assertThat(plot.isFree()).isTrue());
    }

    @Test
    void islandRejectsAPlotCountDifferentFromSixteen() {
        List<CityPlot> fifteenPlots = IntStream.rangeClosed(1, 15)
                .mapToObj(CityPlot::free)
                .toList();

        assertThatThrownBy(() -> new Island(new IslandId(1), new Coordinate(0, 0), "Naxos", LuxuryResource.WINE, fifteenPlots))
                .isInstanceOf(InvalidIslandPlotCountException.class);
    }

    @Test
    void freshIslandsFirstFreePlotIsPlotNumberOne() {
        Island island = Island.withFreePlots(new IslandId(1), new Coordinate(0, 0), "Naxos", LuxuryResource.WINE);

        assertThat(island.firstFreePlot()).isPresent();
        assertThat(island.firstFreePlot().get().number()).isEqualTo(1);
    }
}
