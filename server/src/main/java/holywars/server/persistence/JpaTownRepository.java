package holywars.server.persistence;

import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
class JpaTownRepository implements TownRepository {

    private final SpringDataTownRepository springDataTownRepository;

    JpaTownRepository(SpringDataTownRepository springDataTownRepository) {
        this.springDataTownRepository = springDataTownRepository;
    }

    @Override
    @Transactional
    public void save(Town town) {
        springDataTownRepository.save(TownEntityMapper.toEntity(town));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Town> find(TownId id) {
        return springDataTownRepository.findById(id.value()).map(TownEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Town> findByIsland(IslandId island) {
        return springDataTownRepository.findByIslandIdOrderById(island.value()).stream()
                .map(TownEntityMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Town> findByOwner(PlayerId owner) {
        return springDataTownRepository.findByOwnerIdOrderById(owner.value()).stream()
                .map(TownEntityMapper::toDomain)
                .toList();
    }
}
