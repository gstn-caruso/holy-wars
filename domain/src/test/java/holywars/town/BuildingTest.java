package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BuildingTest {

    @Test
    void buildingRejectsALevelBelowOne() {
        assertThat(new Building(BuildingType.TOWN_HALL, 1).level()).isEqualTo(1);

        assertThatThrownBy(() -> new Building(BuildingType.TOWN_HALL, 0))
                .isInstanceOf(InvalidBuildingLevelException.class);
        assertThatThrownBy(() -> new Building(BuildingType.TOWN_HALL, -1))
                .isInstanceOf(InvalidBuildingLevelException.class);
    }
}
