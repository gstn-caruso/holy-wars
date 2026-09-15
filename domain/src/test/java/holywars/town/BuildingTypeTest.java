package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class BuildingTypeTest {

    @Test
    void everyTypeKnowsItsKindCostsDurationAndSpanishName() {
        assertThat(BuildingType.values())
                .extracting(BuildingType::kind, BuildingType::woodCost, BuildingType::luxuryCost,
                        BuildingType::buildTime, BuildingType::spanishName)
                .containsExactly(
                        tuple(TownPlotKind.TOWN_HALL, 0, 0, Duration.ZERO, "Ayuntamiento"),
                        tuple(TownPlotKind.WALL, 120, 0, Duration.ofMinutes(20), "Muralla"),
                        tuple(TownPlotKind.COAST, 100, 20, Duration.ofMinutes(15), "Puerto comercial"),
                        tuple(TownPlotKind.COAST, 110, 20, Duration.ofMinutes(18), "Astillero"),
                        tuple(TownPlotKind.LAND, 80, 20, Duration.ofMinutes(10), "Academia"),
                        tuple(TownPlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Almacén"),
                        tuple(TownPlotKind.LAND, 50, 10, Duration.ofMinutes(8), "Taberna"),
                        tuple(TownPlotKind.LAND, 100, 0, Duration.ofMinutes(15), "Cuartel"),
                        tuple(TownPlotKind.LAND, 70, 30, Duration.ofMinutes(12), "Templo"),
                        tuple(TownPlotKind.LAND, 90, 10, Duration.ofMinutes(12), "Mercado"),
                        tuple(TownPlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Carpintería"),
                        tuple(TownPlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Viñedo"),
                        tuple(TownPlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Cantería"),
                        tuple(TownPlotKind.LAND, 50, 0, Duration.ofMinutes(7), "Vidriería"),
                        tuple(TownPlotKind.LAND, 60, 20, Duration.ofMinutes(10), "Alquimista"));
    }
}
