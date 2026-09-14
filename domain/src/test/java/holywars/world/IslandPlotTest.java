package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class IslandPlotTest {

    @Test
    void occupyingAFreePlotAssignsItToTheTown() {
        IslandPlot plot = new IslandPlot(1);

        plot.occupy(42L);

        assertThat(plot.isFree()).isFalse();
        assertThat(plot.occupant()).hasValue(42L);
    }
}
