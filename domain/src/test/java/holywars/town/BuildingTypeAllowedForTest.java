package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BuildingTypeAllowedForTest {

    @Test
    void allowedForLandIsEveryLandBuildingType() {
        assertThat(BuildingType.allowedFor(BuildingSlotKind.LAND))
                .containsExactly(
                        BuildingType.ACADEMY,
                        BuildingType.WAREHOUSE,
                        BuildingType.TAVERN,
                        BuildingType.BARRACKS,
                        BuildingType.TEMPLE,
                        BuildingType.MARKET,
                        BuildingType.CARPENTER,
                        BuildingType.WINERY,
                        BuildingType.STONEMASON,
                        BuildingType.GLASSBLOWER,
                        BuildingType.ALCHEMIST);
    }

    @Test
    void allowedForWallIsOnlyWall() {
        assertThat(BuildingType.allowedFor(BuildingSlotKind.WALL))
                .containsExactly(BuildingType.WALL);
    }
}
