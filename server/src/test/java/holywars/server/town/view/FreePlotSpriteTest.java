package holywars.server.town.view;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.town.TownPlotKind;
import org.junit.jupiter.api.Test;

class FreePlotSpriteTest {

    @Test
    void landKindResolvesToThePlotFreeSprite() {
        assertThat(FreePlotSprite.pathFor(TownPlotKind.LAND)).isEqualTo("/img/plot-free.svg");
    }
}
