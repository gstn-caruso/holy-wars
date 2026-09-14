package holywars.world;

import holywars.town.PlotLocation;
import holywars.town.TownId;
import java.util.List;
import java.util.Optional;

public record World(GridSize grid, List<Island> islands) {

    public World {
        islands = List.copyOf(islands);
    }

    public Optional<Island> island(IslandId id) {
        return islands.stream()
                .filter(candidate -> candidate.id().equals(id))
                .findFirst();
    }

    public Optional<Island> islandAt(Coordinate coordinate) {
        return islands.stream()
                .filter(candidate -> candidate.coordinate().equals(coordinate))
                .findFirst();
    }

    public World withCityFounded(PlotLocation location, TownId townId) {
        Island target = island(location.island()).orElseThrow(() -> new UnknownIslandException(location.island()));
        Island founded = target.foundCity(location.plotNumber(), townId);
        List<Island> updatedIslands = islands.stream()
                .map(candidate -> candidate.id().equals(location.island()) ? founded : candidate)
                .toList();
        return new World(grid, updatedIslands);
    }
}
