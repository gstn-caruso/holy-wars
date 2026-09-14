package holywars.world;

import java.util.List;

public final class Island {

    private final IslandId id;
    private final Coordinate coordinate;
    private final String name;
    private final LuxuryResource luxuryResource;
    private final List<IslandPlot> plots;

    public Island(IslandId id, Coordinate coordinate, String name, LuxuryResource luxuryResource,
            List<IslandPlot> plots) {
        this.id = id;
        this.coordinate = coordinate;
        this.name = name;
        this.luxuryResource = luxuryResource;
        this.plots = List.copyOf(plots);
    }

    public IslandId id() {
        return id;
    }

    public Coordinate coordinate() {
        return coordinate;
    }

    public String name() {
        return name;
    }

    public LuxuryResource luxuryResource() {
        return luxuryResource;
    }

    public List<IslandPlot> plots() {
        return plots;
    }

    public IslandPlot firstFreePlot() {
        return plots.stream().filter(IslandPlot::isFree).findFirst().orElseThrow();
    }
}
