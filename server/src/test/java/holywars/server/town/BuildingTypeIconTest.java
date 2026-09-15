package holywars.server.town;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.town.BuildingType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BuildingTypeIconTest {

    @ParameterizedTest
    @CsvSource({
        "TOWN_HALL, /img/building-town-hall.svg",
        "WALL, /img/building-wall.svg",
        "TRADING_PORT, /img/building-trading-port.svg",
        "SHIPYARD, /img/building-shipyard.svg",
        "ACADEMY, /img/building-academy.svg",
        "WAREHOUSE, /img/building-warehouse.svg",
        "TAVERN, /img/building-tavern.svg",
        "BARRACKS, /img/building-barracks.svg",
        "TEMPLE, /img/building-temple.svg",
        "MARKET, /img/building-market.svg",
        "CARPENTER, /img/building-carpenter.svg",
        "WINERY, /img/building-winery.svg",
        "STONEMASON, /img/building-stonemason.svg",
        "GLASSBLOWER, /img/building-glassblower.svg",
        "ALCHEMIST, /img/building-alchemist.svg"
    })
    void resolvesThePathForEachBuildingType(BuildingType type, String expectedPath) {
        assertThat(BuildingTypeIcon.pathFor(type)).isEqualTo(expectedPath);
    }
}
