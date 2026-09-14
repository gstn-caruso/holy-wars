package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlotAnchorConverterTest {

    private final PlotAnchorConverter converter = new PlotAnchorConverter();

    @Test
    void parsesCxCyWidth() {
        PlotAnchor anchor = converter.convert("600,330,140");

        assertThat(anchor).isEqualTo(new PlotAnchor(600, 330, 140));
    }
}
