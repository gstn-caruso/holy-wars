package holywars.server.web;

import holywars.world.LuxuryResource;

public enum LuxuryResourceView {
    WINE("Vino", "resource-wine.svg"),
    MARBLE("Mármol", "resource-marble.svg"),
    CRYSTAL("Cristal", "resource-crystal.svg"),
    SULFUR("Azufre", "resource-sulfur.svg");

    private final String spanishName;
    private final String icon;

    LuxuryResourceView(String spanishName, String icon) {
        this.spanishName = spanishName;
        this.icon = icon;
    }

    static LuxuryResourceView of(LuxuryResource luxury) {
        return valueOf(luxury.name());
    }

    public String spanishName() {
        return spanishName;
    }

    public String icon() {
        return icon;
    }
}
