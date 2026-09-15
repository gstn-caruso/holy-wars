package holywars.town;

import holywars.player.PlayerId;
import java.util.Optional;

public interface Towns {

    Optional<Town> find(TownId id);

    Optional<Town> findByOwner(PlayerId ownerId);

    void save(Town town);
}
