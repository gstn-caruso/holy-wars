package holywars.town;

import java.util.Map;
import java.util.Optional;

class InMemoryTowns implements Towns {

    private final Map<TownId, Town> towns;

    InMemoryTowns(Map<TownId, Town> towns) {
        this.towns = towns;
    }

    @Override
    public Optional<Town> findById(TownId id) {
        return Optional.ofNullable(towns.get(id));
    }
}
