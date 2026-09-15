package holywars.server.view;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.world.LuxuryResource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LuxuryResourceIconTest {

    @ParameterizedTest
    @CsvSource({
        "WINE, /img/resource-wine.svg",
        "MARBLE, /img/resource-marble.svg",
        "CRYSTAL, /img/resource-crystal.svg",
        "SULFUR, /img/resource-sulfur.svg"
    })
    void resolvesThePathForEachLuxuryResource(LuxuryResource resource, String expectedPath) {
        assertThat(LuxuryResourceIcon.pathFor(resource)).isEqualTo(expectedPath);
    }
}
