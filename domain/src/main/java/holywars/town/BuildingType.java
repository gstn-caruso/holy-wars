package holywars.town;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public enum BuildingType {

    TOWN_HALL(TownPlotKind.TOWN_HALL, 0, 0, Duration.ZERO, "Ayuntamiento"),
    WALL(TownPlotKind.WALL, 120, 0, Duration.ofMinutes(20), "Muralla"),
    TRADING_PORT(TownPlotKind.COAST, 100, 20, Duration.ofMinutes(15), "Puerto comercial"),
    SHIPYARD(TownPlotKind.COAST, 110, 20, Duration.ofMinutes(18), "Astillero"),
    ACADEMY(TownPlotKind.LAND, 80, 20, Duration.ofMinutes(10), "Academia"),
    WAREHOUSE(TownPlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Almacén"),
    TAVERN(TownPlotKind.LAND, 50, 10, Duration.ofMinutes(8), "Taberna"),
    BARRACKS(TownPlotKind.LAND, 100, 0, Duration.ofMinutes(15), "Cuartel"),
    TEMPLE(TownPlotKind.LAND, 70, 30, Duration.ofMinutes(12), "Templo"),
    MARKET(TownPlotKind.LAND, 90, 10, Duration.ofMinutes(12), "Mercado"),
    CARPENTER(TownPlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Carpintería"),
    WINERY(TownPlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Viñedo"),
    STONEMASON(TownPlotKind.LAND, 40, 0, Duration.ofMinutes(6), "Cantería"),
    GLASSBLOWER(TownPlotKind.LAND, 50, 0, Duration.ofMinutes(7), "Vidriería"),
    ALCHEMIST(TownPlotKind.LAND, 60, 20, Duration.ofMinutes(10), "Alquimista");

    private final TownPlotKind kind;
    private final int woodCost;
    private final int luxuryCost;
    private final Duration buildTime;
    private final String spanishName;

    BuildingType(TownPlotKind kind, int woodCost, int luxuryCost, Duration buildTime, String spanishName) {
        this.kind = kind;
        this.woodCost = woodCost;
        this.luxuryCost = luxuryCost;
        this.buildTime = buildTime;
        this.spanishName = spanishName;
    }

    public TownPlotKind kind() {
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

    public static List<BuildingType> allowedFor(TownPlotKind kind) {
        return Arrays.stream(values()).filter(type -> type.kind == kind).toList();
    }
}
