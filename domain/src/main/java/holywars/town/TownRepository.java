package holywars.town;

import holywars.player.PlayerId;
import java.util.Optional;

public interface TownRepository {

    Optional<Town> find(TownId id);

    Optional<Town> findByOwner(PlayerId ownerId);

    void save(Town town);
}
