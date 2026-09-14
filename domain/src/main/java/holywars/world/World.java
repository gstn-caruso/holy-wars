package holywars.world;

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
}
