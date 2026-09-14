package holywars.server.web;

import holywars.town.BuildingType;
import java.util.Map;

final class BuildingNames {

    private static final Map<BuildingType, String> SPANISH_NAMES = Map.ofEntries(
            Map.entry(BuildingType.TOWN_HALL, "Ayuntamiento"),
            Map.entry(BuildingType.WALL, "Muralla"),
            Map.entry(BuildingType.TRADING_PORT, "Puerto comercial"),
            Map.entry(BuildingType.SHIPYARD, "Astillero"),
            Map.entry(BuildingType.ACADEMY, "Academia"),
            Map.entry(BuildingType.WAREHOUSE, "Almacén"),
            Map.entry(BuildingType.TAVERN, "Taberna"),
            Map.entry(BuildingType.BARRACKS, "Cuartel"),
            Map.entry(BuildingType.TEMPLE, "Templo"),
            Map.entry(BuildingType.MARKET, "Mercado"),
            Map.entry(BuildingType.CARPENTER, "Carpintería"),
            Map.entry(BuildingType.WINERY, "Viñedo"),
            Map.entry(BuildingType.STONEMASON, "Cantería"),
            Map.entry(BuildingType.GLASSBLOWER, "Vidriería"),
            Map.entry(BuildingType.ALCHEMIST, "Alquimista"));

    private BuildingNames() {
    }

    static String spanishNameOf(BuildingType type) {
        return SPANISH_NAMES.get(type);
    }
}
