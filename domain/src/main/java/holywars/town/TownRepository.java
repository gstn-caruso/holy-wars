package holywars.town;

import java.util.Optional;

public interface TownRepository {

    Optional<Town> find(long townId);

    Optional<Town> findByOwner(long playerId);

    void save(Town town);
}
