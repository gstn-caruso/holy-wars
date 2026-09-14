package holywars.town;

import holywars.player.PlayerId;
import holywars.world.IslandId;
import java.util.List;
import java.util.Optional;

public interface TownRepository {

    void save(Town town);

    Optional<Town> find(TownId id);

    List<Town> findByIsland(IslandId island);

    List<Town> findByOwner(PlayerId owner);
}
