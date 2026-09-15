package holywars.game;

import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class InMemoryTowns implements Towns {

    private final Map<TownId, Town> townsById = new HashMap<>();
    private int saveCount;

    @Override
    public Optional<Town> find(TownId id) {
        return Optional.ofNullable(townsById.get(id));
    }

    @Override
    public Optional<Town> findByOwner(PlayerId ownerId) {
        return townsById.values().stream()
                .filter(town -> town.ownerId().equals(ownerId))
                .findFirst();
    }

    @Override
    public void save(Town town) {
        townsById.put(town.id(), town);
        saveCount++;
    }

    int saveCount() {
        return saveCount;
    }
}
