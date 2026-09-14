package holywars.world;

import java.util.List;
import java.util.Objects;

public final class Island {

    private static final int REQUIRED_PLOT_COUNT = 16;

    private final IslandId id;
    private final Coordinate coordinate;
    private final String name;
    private final LuxuryResource luxuryResource;
    private final List<IslandPlot> plots;

    public Island(IslandId id, Coordinate coordinate, String name, LuxuryResource luxuryResource,
            List<IslandPlot> plots) {
        if (plots.size() != REQUIRED_PLOT_COUNT) {
            throw new InvalidIslandPlotCountException(plots.size());
        }
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Island that)) {
            return false;
        }
        return id.equals(that.id) && coordinate.equals(that.coordinate) && name.equals(that.name)
                && luxuryResource == that.luxuryResource && plots.equals(that.plots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coordinate, name, luxuryResource, plots);
    }
}
