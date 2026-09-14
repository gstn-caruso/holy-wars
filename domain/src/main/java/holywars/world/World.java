package holywars.world;

import java.util.List;
import java.util.Random;

public final class World {

    private final List<Island> islands;

    public World(List<Island> islands) {
        this.islands = List.copyOf(islands);
    }

    public Island island(IslandId id) {
        return islands.stream()
                .filter(island -> island.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new UnknownIslandException(id));
    }

    public Island randomIsland(Random random) {
        return islands.get(random.nextInt(islands.size()));
    }
}
