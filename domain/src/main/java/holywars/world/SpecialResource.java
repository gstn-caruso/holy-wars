package holywars.world;

public enum SpecialResource {
    WINE, MARBLE, CRYSTAL, SULFUR;

    public Resource resource() {
        return Resource.valueOf(name());
    }
}
