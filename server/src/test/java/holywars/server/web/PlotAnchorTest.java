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

    @Test
    void heightForWallScalesWidthByOneElevenOverTwoZeroOne() {
        PlotAnchor anchor = new PlotAnchor(600, 330, 201);

        assertThat(anchor.height(BuildingSlotKind.WALL)).isEqualTo(111);
    }

    @Test
    void xIsCenteredOnCx() {
        PlotAnchor anchor = new PlotAnchor(600, 330, 140);

        assertThat(anchor.x()).isEqualTo(530);
    }
}
