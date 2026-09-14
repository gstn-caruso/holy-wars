package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class LuxuryResourceTest {

    @ParameterizedTest
    @EnumSource(LuxuryResource.class)
    void eachLuxuryResourceKnowsItsSpanishName(LuxuryResource luxuryResource) {
        assertThat(luxuryResource.spanishName()).isEqualTo(switch (luxuryResource) {
            case WINE -> "Vino";
            case MARBLE -> "Mármol";
            case CRYSTAL -> "Cristal";
            case SULFUR -> "Azufre";
        });
    }
}
