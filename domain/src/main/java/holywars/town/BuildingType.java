package holywars.town;

import java.time.Duration;

public enum BuildingType {
    TOWN_HALL(SlotKind.TOWN_HALL, 0, 0, Duration.ZERO),
    WALL(SlotKind.WALL, 120, 0, Duration.ofMinutes(20)),
    TRADING_PORT(SlotKind.COAST, 100, 20, Duration.ofMinutes(15)),
    SHIPYARD(SlotKind.COAST, 110, 20, Duration.ofMinutes(18)),
    ACADEMY(SlotKind.LAND, 80, 20, Duration.ofMinutes(10)),
    WAREHOUSE(SlotKind.LAND, 40, 0, Duration.ofMinutes(6)),
    TAVERN(SlotKind.LAND, 50, 10, Duration.ofMinutes(8)),
    BARRACKS(SlotKind.LAND, 100, 0, Duration.ofMinutes(15)),
    TEMPLE(SlotKind.LAND, 70, 30, Duration.ofMinutes(12)),
    MARKET(SlotKind.LAND, 90, 10, Duration.ofMinutes(12)),
    CARPENTER(SlotKind.LAND, 40, 0, Duration.ofMinutes(6)),
    WINERY(SlotKind.LAND, 40, 0, Duration.ofMinutes(6)),
    STONEMASON(SlotKind.LAND, 40, 0, Duration.ofMinutes(6)),
    GLASSBLOWER(SlotKind.LAND, 50, 0, Duration.ofMinutes(7)),
    ALCHEMIST(SlotKind.LAND, 60, 20, Duration.ofMinutes(10));

    private final SlotKind kind;
    private final int woodCost;
    private final int luxuryCost;
    private final Duration buildTime;

    BuildingType(SlotKind kind, int woodCost, int luxuryCost, Duration buildTime) {
        this.kind = kind;
        this.woodCost = woodCost;
        this.luxuryCost = luxuryCost;
        this.buildTime = buildTime;
    }

    public SlotKind kind() {
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
}
