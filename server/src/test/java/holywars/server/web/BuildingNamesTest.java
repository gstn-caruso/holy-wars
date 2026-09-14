package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.town.BuildingType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class BuildingNamesTest {

    @ParameterizedTest
    @EnumSource(BuildingType.class)
    void everyBuildingTypeHasASpanishName(BuildingType type) {
        assertThat(BuildingNames.spanishNameOf(type)).isNotNull();
    }
}
