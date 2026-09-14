package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BuildingTest {

    @Test
    void aBuildingKnowsItsTypeAndLevel() {
        Building building = new Building(BuildingType.WAREHOUSE, 3);

        assertThat(building.type()).isEqualTo(BuildingType.WAREHOUSE);
        assertThat(building.level()).isEqualTo(3);
    }

    @Test
    void aBuildingLevelBelowOneIsRejected() {
        assertThatThrownBy(() -> new Building(BuildingType.WAREHOUSE, 0))
                .isInstanceOf(InvalidBuildingLevelException.class);
    }
}
