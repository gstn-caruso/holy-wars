package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @ParameterizedTest
    @ValueSource(ints = {0, 17})
    void plotNumberOutsideOneToSixteenIsRejected(int invalidNumber) {
        assertThatThrownBy(() -> CityPlot.free(invalidNumber))
                .isInstanceOf(InvalidCityPlotNumberException.class);
    }
}
