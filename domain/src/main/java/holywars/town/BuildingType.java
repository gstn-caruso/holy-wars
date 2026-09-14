package holywars.town;

public enum BuildingType {
    TOWN_HALL(SlotKind.TOWN_HALL),
    WALL(SlotKind.WALL),
    TRADING_PORT(SlotKind.COAST),
    SHIPYARD(SlotKind.COAST),
    ACADEMY(SlotKind.LAND),
    WAREHOUSE(SlotKind.LAND),
    TAVERN(SlotKind.LAND),
    BARRACKS(SlotKind.LAND),
    TEMPLE(SlotKind.LAND),
    MARKET(SlotKind.LAND),
    CARPENTER(SlotKind.LAND),
    WINERY(SlotKind.LAND),
    STONEMASON(SlotKind.LAND),
    GLASSBLOWER(SlotKind.LAND),
    ALCHEMIST(SlotKind.LAND);

    private final SlotKind kind;

    BuildingType(SlotKind kind) {
        this.kind = kind;
    }

    public SlotKind kind() {
        return kind;
    }
}
