package holywars.server.town.view;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.town.TownPlotKind;
import org.junit.jupiter.api.Test;

class FreePlotSpriteTest {

    @Test
    void landKindResolvesToThePlotFreeSprite() {
        assertThat(FreePlotSprite.pathFor(TownPlotKind.LAND)).isEqualTo("/img/plot-free.svg");
    }

    @Test
    void coastKindResolvesToThePlotFreeCoastSprite() {
        assertThat(FreePlotSprite.pathFor(TownPlotKind.COAST)).isEqualTo("/img/plot-free-coast.svg");
    }
}
