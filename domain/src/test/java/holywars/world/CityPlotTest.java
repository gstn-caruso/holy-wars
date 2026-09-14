package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CityPlotTest {

    @Test
    void freePlotKnowsItsNumberAndIsFree() {
        CityPlot plot = CityPlot.free(5);

        assertThat(plot.number()).isEqualTo(5);
        assertThat(plot.isFree()).isTrue();
    }
}
