package holywars.server.persistence;

import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class TownJpaAdapter implements TownRepository {

    private final TownJpaRepository townJpaRepository;

    TownJpaAdapter(TownJpaRepository townJpaRepository) {
        this.townJpaRepository = townJpaRepository;
    }

    @Override
    public Optional<Town> find(TownId id) {
        return townJpaRepository.findById(id.value()).map(this::toDomain);
    }

    @Override
    public Optional<Town> findByOwner(PlayerId ownerId) {
        return townJpaRepository.findByOwnerId(ownerId.value()).map(this::toDomain);
    }

    @Override
    public void save(Town town) {
        townJpaRepository.save(new TownEntity(
                town.id().value(),
                town.ownerId().value(),
                town.islandId().value(),
                town.plotNumber(),
                town.name()));
    }

    private Town toDomain(TownEntity entity) {
        return new Town(
                new TownId(entity.id()),
                new PlayerId(entity.ownerId()),
                new IslandId(entity.islandId()),
                entity.plotNumber(),
                entity.name());
    }
}
