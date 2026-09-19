package holywars.world;

public enum SpecialResource {

    WINE(Resource.WINE),
    MARBLE(Resource.MARBLE),
    CRYSTAL(Resource.CRYSTAL),
    SULFUR(Resource.SULFUR);

    private final Resource resource;

    SpecialResource(Resource resource) {
        this.resource = resource;
    }

    public Resource resource() {
        return resource;
    }
}
