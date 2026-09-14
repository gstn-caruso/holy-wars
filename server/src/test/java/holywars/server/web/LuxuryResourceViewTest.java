package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import holywars.world.LuxuryResource;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class LuxuryResourceViewTest {

    @ParameterizedTest
    @EnumSource(LuxuryResource.class)
    void everyLuxuryResourceHasAView(LuxuryResource luxury) {
        assertThatCode(() -> LuxuryResourceView.of(luxury)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("luxuryResources")
    void knowsTheSpanishNameAndIconOfEachLuxuryResource(LuxuryResource luxury, String spanishName, String icon) {
        LuxuryResourceView view = LuxuryResourceView.of(luxury);

        assertThat(view.spanishName()).isEqualTo(spanishName);
        assertThat(view.icon()).isEqualTo(icon);
    }

    private static Stream<Arguments> luxuryResources() {
        return Stream.of(
                Arguments.of(LuxuryResource.WINE, "Vino", "resource-wine.svg"),
                Arguments.of(LuxuryResource.MARBLE, "Mármol", "resource-marble.svg"),
                Arguments.of(LuxuryResource.CRYSTAL, "Cristal", "resource-crystal.svg"),
                Arguments.of(LuxuryResource.SULFUR, "Azufre", "resource-sulfur.svg"));
    }
}
