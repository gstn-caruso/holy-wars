package holywars.world;

import java.util.Optional;

public interface WorldRepository {

    Optional<World> find();

    void save(World world);
}
