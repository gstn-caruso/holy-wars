package holywars.server.world;

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

    @Test
    void occupiedPlotCarriesTownOwnerAndLink() {
        PlotView plot = PlotView.occupied(1, "Atenas", "Jugador", 42L);

        assertThat(plot.number()).isEqualTo(1);
        assertThat(plot.occupied()).isTrue();
        assertThat(plot.townName()).isEqualTo("Atenas");
        assertThat(plot.ownerName()).isEqualTo("Jugador");
        assertThat(plot.townId()).isEqualTo(42L);
    }
}
