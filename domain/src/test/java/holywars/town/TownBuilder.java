package holywars.town;

import holywars.world.IslandId;

class TownBuilder {

    private TownId id = new TownId(10L);
    private String name = "Sparta";
    private IslandId islandId = new IslandId(1L);
    private ResourceStock resourceStock = ResourceStock.empty();

    static TownBuilder aTown() {
        return new TownBuilder();
    }

    TownBuilder withId(TownId id) {
        this.id = id;
        return this;
    }

    TownBuilder withName(String name) {
        this.name = name;
        return this;
    }

    TownBuilder onIsland(IslandId islandId) {
        this.islandId = islandId;
        return this;
    }

    TownBuilder stocking(ResourceStock resourceStock) {
        this.resourceStock = resourceStock;
        return this;
    }

    Town build() {
        return new Town(id, name, islandId, resourceStock);
    }
}
