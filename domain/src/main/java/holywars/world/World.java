package holywars.world;

import java.util.List;
import java.util.Optional;

public record World(List<Island> islands) {

    public World {
        islands = List.copyOf(islands);
    }

    public Optional<Island> island(IslandId id) {
        return islands.stream()
                .filter(candidate -> candidate.id().equals(id))
                .findFirst();
    }
}
