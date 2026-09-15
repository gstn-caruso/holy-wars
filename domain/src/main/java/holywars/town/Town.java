package holywars.town;

import holywars.player.PlayerId;
import holywars.resources.TownResources;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public record Town(TownId id, PlayerId ownerId, IslandId islandId, int plotNumber, String name,
        List<TownPlot> townPlots, TownResources resources) {

    static final int REQUIRED_BUILDING_PLOT_COUNT = 14;

    public Town {
        long distinctPositions = townPlots.stream().map(TownPlot::position).distinct().count();
        boolean hasExactlyTheRequiredDistinctPositions = townPlots.size() == REQUIRED_BUILDING_PLOT_COUNT
                && distinctPositions == REQUIRED_BUILDING_PLOT_COUNT;
        if (!hasExactlyTheRequiredDistinctPositions) {
            throw new InvalidTownPlotCountException((int) distinctPositions);
        }
        boolean hasAnOccupiedTownHall = townPlots.stream().anyMatch(TownPlot::isOccupiedTownHall);
        if (!hasAnOccupiedTownHall) {
            throw new MissingTownHallException();
        }
        townPlots = List.copyOf(townPlots);
    }

    public static Town founded(TownId id, PlayerId ownerId, IslandId islandId, int plotNumber, String name,
            LuxuryResource luxuryResource, Instant foundedAt) {
        return new Town(id, ownerId, islandId, plotNumber, name, TownPlots.standard(),
                TownResources.starting(luxuryResource, foundedAt));
    }

    public static Town reconstituted(TownId id, PlayerId ownerId, IslandId islandId, int plotNumber, String name,
            List<TownPlot> townPlots, TownResources resources) {
        return new Town(id, ownerId, islandId, plotNumber, name, townPlots, resources);
    }

    public Town advancedTo(Instant now) {
        List<TownPlot> advancedPlots = townPlots.stream().map(plot -> plot.advancedTo(now)).toList();
        return new Town(id, ownerId, islandId, plotNumber, name, advancedPlots, resources.advancedTo(now));
    }

    public Town spend(int wood, int luxury) {
        return new Town(id, ownerId, islandId, plotNumber, name, townPlots, resources.spend(wood, luxury));
    }

    public Town startingConstruction(int position, BuildingType type, Instant now) {
        Town advanced = advancedTo(now);
        TownPlot plot = advanced.plot(position);
        TownPlot underConstruction = plot.startingConstruction(type, advanced.townHallLevel(), now);
        List<TownPlot> updatedPlots = advanced.townPlots.stream()
                .map(existing -> existing.position() == position ? underConstruction : existing)
                .toList();
        TownResources spentResources = advanced.resources.spend(type.woodCost(), type.luxuryCost());
        return new Town(advanced.id, advanced.ownerId, advanced.islandId, advanced.plotNumber, advanced.name,
                updatedPlots, spentResources);
    }

    public int townHallLevel() {
        return townPlots.stream()
                .filter(TownPlot::isOccupiedTownHall)
                .findFirst()
                .orElseThrow()
                .builtLevel();
    }

    public Optional<Instant> nextFinishAt() {
        return townPlots.stream()
                .map(TownPlot::finishesAt)
                .flatMap(Optional::stream)
                .min(Instant::compareTo);
    }

    public TownPlot plot(int position) {
        return townPlots.stream()
                .filter(plot -> plot.position() == position)
                .findFirst()
                .orElseThrow(() -> new InvalidTownPlotPositionException(position));
    }
}
