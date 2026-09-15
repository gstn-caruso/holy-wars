package holywars.server.town;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.support.PropertiesLoaderUtils;

class TownScenePropertiesDevProfileTest {

    @Test
    void devProfileBindsAFullNineteenTwentyByTwelveHundredLayout() throws IOException {
        Properties devProperties = PropertiesLoaderUtils.loadAllProperties("application-dev.properties");
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new PlotAnchorConverter());
        Binder binder = new Binder(List.of(new MapConfigurationPropertySource(devProperties)), null,
                conversionService);

        TownSceneProperties layout = binder.bind("holywars.town-scene", TownSceneProperties.class).get();

        assertThat(layout.width()).isEqualTo(1920);
        assertThat(layout.height()).isEqualTo(1200);
        assertThat(layout.anchorFor(12)).isEqualTo(new PlotAnchor(500, 352, 201));
    }
}
