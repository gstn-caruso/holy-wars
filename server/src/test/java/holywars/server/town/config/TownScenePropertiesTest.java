package holywars.server.town.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.server.town.errors.IncompleteTownSceneLayoutException;
import holywars.server.town.errors.InvalidPlotAnchorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.convert.support.DefaultConversionService;

class TownScenePropertiesTest {

    private static final Map<String, String> STANDARD_LAYOUT = Map.ofEntries(
            Map.entry("holywars.town-scene.width", "1200"),
            Map.entry("holywars.town-scene.height", "720"),
            Map.entry("holywars.town-scene.plots.1", "600,330,140"),
            Map.entry("holywars.town-scene.plots.2", "600,180,140"),
            Map.entry("holywars.town-scene.plots.3", "520,520,140"),
            Map.entry("holywars.town-scene.plots.4", "680,520,140"),
            Map.entry("holywars.town-scene.plots.5", "400,250,140"),
            Map.entry("holywars.town-scene.plots.6", "800,250,140"),
            Map.entry("holywars.town-scene.plots.7", "380,420,140"),
            Map.entry("holywars.town-scene.plots.8", "820,420,140"),
            Map.entry("holywars.town-scene.plots.9", "715,118,140"),
            Map.entry("holywars.town-scene.plots.10", "300,330,140"),
            Map.entry("holywars.town-scene.plots.11", "900,330,140"),
            Map.entry("holywars.town-scene.plots.12", "600,117,164"),
            Map.entry("holywars.town-scene.plots.13", "250,560,140"),
            Map.entry("holywars.town-scene.plots.14", "400,640,140"));

    @Test
    void bindsWidthHeightAndTheFourteenPlotAnchors() {
        TownSceneProperties layout = bind(STANDARD_LAYOUT);

        assertThat(layout.width()).isEqualTo(1200);
        assertThat(layout.height()).isEqualTo(720);
        assertThat(layout.anchorFor(12)).isEqualTo(new PlotAnchor(600, 117, 164));
    }

    @Test
    void missingAPositionFailsWithIncompleteTownSceneLayout() {
        Map<String, String> incomplete = new HashMap<>(STANDARD_LAYOUT);
        incomplete.remove("holywars.town-scene.plots.13");

        assertThatThrownBy(() -> bind(incomplete))
                .isInstanceOf(BindException.class)
                .hasRootCauseInstanceOf(IncompleteTownSceneLayoutException.class);
    }

    @Test
    void extraPositionFailsWithIncompleteTownSceneLayout() {
        Map<String, String> withExtra = new HashMap<>(STANDARD_LAYOUT);
        withExtra.put("holywars.town-scene.plots.15", "0,0,10");

        assertThatThrownBy(() -> bind(withExtra))
                .isInstanceOf(BindException.class)
                .hasRootCauseInstanceOf(IncompleteTownSceneLayoutException.class);
    }

    @Test
    void missingEveryPositionFailsWithIncompleteTownSceneLayout() {
        Map<String, String> noPlots = Map.of(
                "holywars.town-scene.width", "1200",
                "holywars.town-scene.height", "720");

        assertThatThrownBy(() -> bind(noPlots))
                .isInstanceOf(BindException.class)
                .hasRootCauseInstanceOf(IncompleteTownSceneLayoutException.class);
    }

    @Test
    void plotsIsImmutableToCallers() {
        TownSceneProperties layout = bind(STANDARD_LAYOUT);

        assertThatThrownBy(() -> layout.plots().put(99, new PlotAnchor(0, 0, 0)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void invalidAnchorFormatFailsWithInvalidPlotAnchor() {
        Map<String, String> invalid = new HashMap<>(STANDARD_LAYOUT);
        invalid.put("holywars.town-scene.plots.13", "not-an-anchor");

        assertThatThrownBy(() -> bind(invalid))
                .isInstanceOf(BindException.class)
                .hasRootCauseInstanceOf(InvalidPlotAnchorException.class);
    }

    private TownSceneProperties bind(Map<String, String> properties) {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new PlotAnchorConverter());
        Binder binder = new Binder(List.of(new MapConfigurationPropertySource(properties)), null, conversionService);
        return binder.bind("holywars.town-scene", TownSceneProperties.class).get();
    }
}
