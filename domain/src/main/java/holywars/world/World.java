package holywars.world;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class World {

    private final List<Island> islands;

    public World(List<Island> islands) {
        this.islands = List.copyOf(islands);
    }

    public List<Island> islands() {
        return islands;
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof World that)) {
            return false;
        }
        return islands.equals(that.islands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(islands);
    }
}
