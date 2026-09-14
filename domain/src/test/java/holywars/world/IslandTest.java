package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class IslandTest {

    @Test
    void islandExposesIdCoordinateNameResourceAndPlots() {
        IslandId id = new IslandId(1);
        Coordinate coordinate = new Coordinate(3, 4);
        List<CityPlot> plots = sixteenFreePlots();

        Island island = new Island(id, coordinate, "Naxos", LuxuryResource.WINE, plots);

        assertThat(island.id()).isEqualTo(id);
        assertThat(island.coordinate()).isEqualTo(coordinate);
        assertThat(island.name()).isEqualTo("Naxos");
        assertThat(island.resource()).isEqualTo(LuxuryResource.WINE);
        assertThat(island.plots()).isEqualTo(plots);
    }

    @Test
    void islandRejectsAPlotCountDifferentFromSixteen() {
        List<CityPlot> fifteenPlots = IntStream.rangeClosed(1, 15)
                .mapToObj(CityPlot::free)
                .toList();

        assertThatThrownBy(() -> new Island(new IslandId(1), new Coordinate(0, 0), "Naxos", LuxuryResource.WINE, fifteenPlots))
                .isInstanceOf(InvalidIslandPlotCountException.class);
    }

    private List<CityPlot> sixteenFreePlots() {
        return IntStream.rangeClosed(1, 16)
                .mapToObj(CityPlot::free)
                .toList();
    }
}
