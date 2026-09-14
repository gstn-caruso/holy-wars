package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlotViewTest {

    @Test
    void freePlotCarriesNoTownNorOwner() {
        PlotView plot = PlotView.free(3);

        assertThat(plot.number()).isEqualTo(3);
        assertThat(plot.occupied()).isFalse();
        assertThat(plot.townName()).isNull();
        assertThat(plot.ownerName()).isNull();
    }
}
