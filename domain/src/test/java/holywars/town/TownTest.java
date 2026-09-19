package holywars.town;

import holywars.world.IslandId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TownTest {

    @Test
    void keepsItsPlotsSafeFromOutsideChanges() {
        List<TownPlot> originalPlots = new ArrayList<>(List.of(TownPlot.empty(0)));
        Town town = new Town(new TownId(1L), "Sparta", new IslandId(1L), ResourceStock.empty(), originalPlots);

        originalPlots.add(TownPlot.empty(1));

        assertThat(town.plots()).containsExactly(TownPlot.empty(0));
        assertThatThrownBy(() -> town.plots().add(TownPlot.empty(2)))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
