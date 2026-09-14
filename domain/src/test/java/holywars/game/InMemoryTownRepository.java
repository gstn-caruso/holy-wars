package holywars.game;

import holywars.town.Town;
import holywars.town.TownRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class InMemoryTownRepository implements TownRepository {

    private final Map<Long, Town> townsById = new HashMap<>();
    private int saveCount;

    @Override
    public Optional<Town> find(long townId) {
        return Optional.ofNullable(townsById.get(townId));
    }

    @Override
    public Optional<Town> findByOwner(long playerId) {
        return townsById.values().stream()
                .filter(town -> town.ownerId().value() == playerId)
                .findFirst();
    }

    @Override
    public void save(Town town) {
        townsById.put(town.id().value(), town);
        saveCount++;
    }

    int saveCount() {
        return saveCount;
    }
}
