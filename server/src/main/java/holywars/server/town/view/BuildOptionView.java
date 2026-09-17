package holywars.server.town.view;

import holywars.server.view.LuxuryResourceIcon;
import holywars.town.BuildingType;
import holywars.world.LuxuryResource;

record BuildOptionView(String type, String name, String spriteHref, int woodCost, int luxuryCost, long minutes,
        String luxuryIconPath) {

    static BuildOptionView of(BuildingType type, LuxuryResource luxuryResource) {
        return new BuildOptionView(type.name(), type.spanishName(), BuildingTypeIcon.pathFor(type), type.woodCost(),
                type.luxuryCost(), type.buildTime().toMinutes(), luxuryIconFor(type, luxuryResource));
    }

    private static String luxuryIconFor(BuildingType type, LuxuryResource luxuryResource) {
        return type.luxuryCost() > 0 ? LuxuryResourceIcon.pathFor(luxuryResource) : null;
    }
}
