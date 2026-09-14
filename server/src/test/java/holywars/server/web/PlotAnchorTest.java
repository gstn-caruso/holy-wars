package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PlotAnchorTest {

    @Test
    void parsesCxCyAndWidthFromItsTextRepresentation() {
        PlotAnchor anchor = PlotAnchor.parse("928,537,172");

        assertThat(anchor).isEqualTo(new PlotAnchor(928, 537, 172));
    }

    @Test
    void rejectsATextRepresentationWithMissingValues() {
        assertThatThrownBy(() -> PlotAnchor.parse("928,537"))
                .isInstanceOf(InvalidPlotAnchorException.class);
    }

    @Test
    void rejectsATextRepresentationWithANonNumericValue() {
        assertThatThrownBy(() -> PlotAnchor.parse("928,abc,172"))
                .isInstanceOf(InvalidPlotAnchorException.class);
    }
}
