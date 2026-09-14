package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class BuildingTypeTest {

    @Test
    void everyBuildingTypeKnowsItsLevelOneCostAndBuildTime() {
        assertThat(BuildingType.ACADEMY.woodCost()).isEqualTo(80);
        assertThat(BuildingType.ACADEMY.luxuryCost()).isEqualTo(20);
        assertThat(BuildingType.ACADEMY.buildTime()).isEqualTo(Duration.ofMinutes(10));

        assertThat(BuildingType.TOWN_HALL.woodCost()).isEqualTo(0);
        assertThat(BuildingType.TOWN_HALL.luxuryCost()).isEqualTo(0);
        assertThat(BuildingType.TOWN_HALL.buildTime()).isEqualTo(Duration.ZERO);
    }
}
