package holywars.world;

public enum LuxuryResource {
    WINE("Vino"),
    MARBLE("Mármol"),
    CRYSTAL("Cristal"),
    SULFUR("Azufre");

    private final String spanishName;

    LuxuryResource(String spanishName) {
        this.spanishName = spanishName;
    }

    public String spanishName() {
        return spanishName;
    }
}
