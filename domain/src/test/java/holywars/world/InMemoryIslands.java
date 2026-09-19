package holywars.world;

import java.util.Map;
import java.util.Optional;

public class InMemoryIslands implements Islands {

    private final Map<IslandId, Island> islands;

    public InMemoryIslands(Map<IslandId, Island> islands) {
        this.islands = islands;
    }

    @Override
    public Optional<Island> findById(IslandId id) {
        return Optional.ofNullable(islands.get(id));
    }
}
