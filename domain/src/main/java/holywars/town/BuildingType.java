package holywars.town;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public enum BuildingType {

    TOWN_HALL(BuildingSlotKind.TOWN_HALL, 0, 0, Duration.ZERO, "Ayuntamiento"),
    WALL(BuildingSlotKind.WALL, 120, 0, Duration.ofMinutes(20), "Muralla"),
    TRADING_PORT(BuildingSlotKind.COAST, 100, 20, Duration.ofMinutes(15), "Puerto comercial"),
    SHIPYARD(BuildingSlotKind.COAST, 110, 20, Duration.ofMinutes(18), "Astillero"),
    ACADEMY(BuildingSlotKind.LAND, 80, 20, Duration.ofMinutes(10), "Academia"),
    WAREHOUSE(BuildingSlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Almacén"),
    TAVERN(BuildingSlotKind.LAND, 50, 10, Duration.ofMinutes(8), "Taberna"),
    BARRACKS(BuildingSlotKind.LAND, 100, 0, Duration.ofMinutes(15), "Cuartel"),
    TEMPLE(BuildingSlotKind.LAND, 70, 30, Duration.ofMinutes(12), "Templo"),
    MARKET(BuildingSlotKind.LAND, 90, 10, Duration.ofMinutes(12), "Mercado"),
    CARPENTER(BuildingSlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Carpintería"),
    WINERY(BuildingSlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Viñedo"),
    STONEMASON(BuildingSlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Cantería"),
    GLASSBLOWER(BuildingSlotKind.LAND, 50, 0, Duration.ofMinutes(7), "Vidriería"),
    ALCHEMIST(BuildingSlotKind.LAND, 60, 20, Duration.ofMinutes(10), "Alquimista");

    private final BuildingSlotKind kind;
    private final int woodCost;
    private final int luxuryCost;
    private final Duration buildTime;
    private final String spanishName;

    BuildingType(BuildingSlotKind kind, int woodCost, int luxuryCost, Duration buildTime, String spanishName) {
        this.kind = kind;
        this.woodCost = woodCost;
        this.luxuryCost = luxuryCost;
        this.buildTime = buildTime;
        this.spanishName = spanishName;
    }

    public BuildingSlotKind kind() {
        return kind;
    }

    public int woodCost() {
        return woodCost;
    }

    public int luxuryCost() {
        return luxuryCost;
    }

    public Duration buildTime() {
        return buildTime;
    }

    public String spanishName() {
        return spanishName;
    }

    public static List<BuildingType> allowedFor(BuildingSlotKind kind) {
        return Arrays.stream(values()).filter(type -> type.kind == kind).toList();
    }
}
