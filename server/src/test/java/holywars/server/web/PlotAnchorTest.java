package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlotAnchorTest {

    @Test
    void parsesCxCyAndWidthFromItsTextRepresentation() {
        PlotAnchor anchor = PlotAnchor.parse("928,537,172");

        assertThat(anchor).isEqualTo(new PlotAnchor(928, 537, 172));
    }
}
