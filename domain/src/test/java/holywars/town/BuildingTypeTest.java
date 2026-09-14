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
                        tuple(BuildingSlotKind.TOWN_HALL, 0, 0, Duration.ZERO, "Ayuntamiento"),
                        tuple(BuildingSlotKind.WALL, 120, 0, Duration.ofMinutes(20), "Muralla"),
                        tuple(BuildingSlotKind.COAST, 100, 20, Duration.ofMinutes(15), "Puerto comercial"),
                        tuple(BuildingSlotKind.COAST, 110, 20, Duration.ofMinutes(18), "Astillero"),
                        tuple(BuildingSlotKind.LAND, 80, 20, Duration.ofMinutes(10), "Academia"),
                        tuple(BuildingSlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Almacén"),
                        tuple(BuildingSlotKind.LAND, 50, 10, Duration.ofMinutes(8), "Taberna"),
                        tuple(BuildingSlotKind.LAND, 100, 0, Duration.ofMinutes(15), "Cuartel"),
                        tuple(BuildingSlotKind.LAND, 70, 30, Duration.ofMinutes(12), "Templo"),
                        tuple(BuildingSlotKind.LAND, 90, 10, Duration.ofMinutes(12), "Mercado"),
                        tuple(BuildingSlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Carpintería"),
                        tuple(BuildingSlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Viñedo"),
                        tuple(BuildingSlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Cantería"),
                        tuple(BuildingSlotKind.LAND, 50, 0, Duration.ofMinutes(7), "Vidriería"),
                        tuple(BuildingSlotKind.LAND, 60, 20, Duration.ofMinutes(10), "Alquimista"));
    }
}
