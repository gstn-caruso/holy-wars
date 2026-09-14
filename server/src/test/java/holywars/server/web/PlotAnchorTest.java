package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.town.BuildingSlotKind;
import org.junit.jupiter.api.Test;

class PlotAnchorTest {

    @Test
    void heightForLandScalesWidthByOneFortyOverOneSeventyTwo() {
        PlotAnchor anchor = new PlotAnchor(600, 330, 172);

        assertThat(anchor.height(BuildingSlotKind.LAND)).isEqualTo(140);
    }
}
