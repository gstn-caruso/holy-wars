package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.town.TownId;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CityPlotTest {

    @Test
    void freePlotKnowsItsNumberAndIsFree() {
        CityPlot plot = CityPlot.free(5);

        assertThat(plot.number()).isEqualTo(5);
        assertThat(plot.isFree()).isTrue();
    }

    @Test
    void freePlotIsFreeAndHasNoTown() {
        CityPlot plot = CityPlot.free(5);

        assertThat(plot.isFree()).isTrue();
        assertThat(plot.town()).isEqualTo(Optional.empty());
    }

    @Test
    void foundingAFreePlotMakesItOccupiedByThatTown() {
        CityPlot plot = CityPlot.free(5);
        TownId townId = new TownId(1);

        CityPlot founded = plot.foundedBy(townId);

        assertThat(founded.isFree()).isFalse();
        assertThat(founded.town()).isEqualTo(Optional.of(townId));
        assertThat(founded.number()).isEqualTo(5);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 17})
    void plotNumberOutsideOneToSixteenIsRejected(int invalidNumber) {
        assertThatThrownBy(() -> CityPlot.free(invalidNumber))
                .isInstanceOf(InvalidCityPlotNumberException.class);
    }

    @Test
    void foundingAnAlreadyOccupiedPlotIsRejected() {
        CityPlot occupied = CityPlot.free(5).foundedBy(new TownId(1));

        assertThatThrownBy(() -> occupied.foundedBy(new TownId(2)))
                .isInstanceOf(PlotAlreadyOccupiedException.class);
    }
}
