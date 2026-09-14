package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class IslandTest {

    @Test
    void islandExposesIdCoordinateNameResourceAndPlots() {
        IslandId id = new IslandId(1);
        Coordinate coordinate = new Coordinate(3, 4);
        List<CityPlot> plots = IntStream.rangeClosed(1, 16)
                .mapToObj(CityPlot::free)
                .toList();

        Island island = new Island(id, coordinate, "Naxos", LuxuryResource.WINE, plots);

        assertThat(island.id()).isEqualTo(id);
        assertThat(island.coordinate()).isEqualTo(coordinate);
        assertThat(island.name()).isEqualTo("Naxos");
        assertThat(island.resource()).isEqualTo(LuxuryResource.WINE);
        assertThat(island.plots()).isEqualTo(plots);
    }
}
