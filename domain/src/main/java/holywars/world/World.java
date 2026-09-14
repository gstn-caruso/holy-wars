package holywars.world;

import java.util.List;

public final class World {

    private final List<Island> islands;

    public World(List<Island> islands) {
        this.islands = List.copyOf(islands);
    }

    public Island island(IslandId id) {
        return islands.stream()
                .filter(island -> island.id().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
