package holywars.world;

import java.util.Optional;

public interface Worlds {

    Optional<World> find();

    void save(World world);
}
