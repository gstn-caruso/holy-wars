package holywars.town;

import holywars.world.IslandId;

import java.util.List;

class TownBuilder {

    private final TownId id;
    private final String name;
    private final IslandId islandId;
    private ResourceStock resourceStock = ResourceStock.empty();
    private List<TownPlot> plots = List.of();

    private TownBuilder(TownId id, String name, IslandId islandId) {
        this.id = id;
        this.name = name;
        this.islandId = islandId;
    }

    static TownBuilder aTown(TownId id, String name, IslandId islandId) {
        return new TownBuilder(id, name, islandId);
    }

    TownBuilder stocking(ResourceStock resourceStock) {
        this.resourceStock = resourceStock;
        return this;
    }

    TownBuilder withPlots(TownPlot... plots) {
        this.plots = List.of(plots);
        return this;
    }

    Town build() {
        return new Town(id, name, islandId, resourceStock, plots);
    }
}
