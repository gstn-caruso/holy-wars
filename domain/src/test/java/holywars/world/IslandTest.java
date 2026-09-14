package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.town.TownId;
import java.util.List;
import java.util.Optional;
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

    @Test
    void foundingACityOccupiesTheChosenPlotAndLeavesOthersFree() {
        Island island = Island.withFreePlots(new IslandId(1), new Coordinate(0, 0), "Naxos", LuxuryResource.WINE);
        TownId townId = new TownId(1);

        Island founded = island.foundCity(3, townId);

        CityPlot occupiedPlot = founded.plots().stream().filter(plot -> plot.number() == 3).findFirst().orElseThrow();
        assertThat(occupiedPlot.isFree()).isFalse();
        assertThat(occupiedPlot.town()).isEqualTo(Optional.of(townId));
        assertThat(founded.plots().stream().filter(plot -> plot.number() != 3)).allSatisfy(plot -> assertThat(plot.isFree()).isTrue());
    }

    @Test
    void firstFreePlotIsEmptyWhenAllPlotsAreOccupied() {
        Island island = Island.withFreePlots(new IslandId(1), new Coordinate(0, 0), "Naxos", LuxuryResource.WINE);

        for (int plotNumber = 1; plotNumber <= CityPlot.HIGHEST_NUMBER; plotNumber++) {
            island = island.foundCity(plotNumber, new TownId(plotNumber));
        }

        assertThat(island.firstFreePlot()).isEmpty();
    }

    @Test
    void foundingAnOutOfRangePlotNumberIsRejected() {
        Island island = Island.withFreePlots(new IslandId(1), new Coordinate(0, 0), "Naxos", LuxuryResource.WINE);

        assertThatThrownBy(() -> island.foundCity(17, new TownId(1)))
                .isInstanceOf(InvalidCityPlotNumberException.class);
    }

    @Test
    void foundingAnAlreadyOccupiedPlotThroughTheIslandIsRejected() {
        Island island = Island.withFreePlots(new IslandId(1), new Coordinate(0, 0), "Naxos", LuxuryResource.WINE)
                .foundCity(3, new TownId(1));

        assertThatThrownBy(() -> island.foundCity(3, new TownId(2)))
                .isInstanceOf(PlotAlreadyOccupiedException.class);
    }
}
