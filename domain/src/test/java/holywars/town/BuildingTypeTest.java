package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;
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

    @Test
    void forKindListsEveryTypeOfThatKindAndNoOther() {
        assertThat(BuildingType.forKind(SlotKind.WALL)).containsExactly(BuildingType.WALL);
        assertThat(BuildingType.forKind(SlotKind.COAST))
                .containsExactlyInAnyOrder(BuildingType.TRADING_PORT, BuildingType.SHIPYARD);

        List<BuildingType> everyTypeGroupedByKind = Stream.of(SlotKind.values())
                .flatMap(kind -> BuildingType.forKind(kind).stream())
                .toList();

        assertThat(everyTypeGroupedByKind).containsExactlyInAnyOrderElementsOf(List.of(BuildingType.values()));
    }
}
