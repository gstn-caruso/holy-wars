package holywars.world;

import holywars.town.TownId;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public record Island(IslandId id, Coordinate coordinate, String name, LuxuryResource resource, List<CityPlot> plots) {

    public Island {
        plots = List.copyOf(plots);
        if (plots.size() != CityPlot.HIGHEST_NUMBER) {
            throw new InvalidIslandPlotCountException(plots.size());
        }
    }

    public static Island withFreePlots(IslandId id, Coordinate coordinate, String name, LuxuryResource resource) {
        List<CityPlot> freePlots = IntStream.rangeClosed(1, CityPlot.HIGHEST_NUMBER)
                .mapToObj(CityPlot::free)
                .toList();
        return new Island(id, coordinate, name, resource, freePlots);
    }

    public Optional<CityPlot> firstFreePlot() {
        return plots.stream().filter(CityPlot::isFree).findFirst();
    }

    public Island foundCity(int plotNumber, TownId townId) {
        CityPlot chosenPlot = plots.stream()
                .filter(plot -> plot.number() == plotNumber)
                .findFirst()
                .orElseThrow(() -> new InvalidCityPlotNumberException(plotNumber));
        List<CityPlot> updatedPlots = plots.stream()
                .map(plot -> plot.number() == plotNumber ? chosenPlot.foundedBy(townId) : plot)
                .toList();
        return new Island(id, coordinate, name, resource, updatedPlots);
    }
}
