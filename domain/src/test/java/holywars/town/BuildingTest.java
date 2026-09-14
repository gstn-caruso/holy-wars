package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BuildingTest {

    @Test
    void aBuildingKnowsItsTypeAndLevel() {
        Building building = new Building(BuildingType.WAREHOUSE, 3);

        assertThat(building.type()).isEqualTo(BuildingType.WAREHOUSE);
        assertThat(building.level()).isEqualTo(3);
    }
}
