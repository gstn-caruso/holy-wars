package holywars.game;

import holywars.world.World;
import holywars.world.Worlds;
import java.util.Optional;

final class InMemoryWorlds implements Worlds {

    private World world;
    private int saveCount;

    @Override
    public Optional<World> find() {
        return Optional.ofNullable(world);
    }

    @Override
    public void save(World world) {
        this.world = world;
        saveCount++;
    }

    int saveCount() {
        return saveCount;
    }
}
