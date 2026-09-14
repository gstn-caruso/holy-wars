package holywars.server.persistence;

import holywars.world.World;
import holywars.world.WorldRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
class JpaWorldRepository implements WorldRepository {

    private static final Integer WORLD_ID = 1;

    private final SpringDataWorldRepository springDataWorldRepository;

    JpaWorldRepository(SpringDataWorldRepository springDataWorldRepository) {
        this.springDataWorldRepository = springDataWorldRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<World> find() {
        return springDataWorldRepository.findById(WORLD_ID).map(WorldEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public void save(World world) {
        springDataWorldRepository.save(WorldEntityMapper.toEntity(world, WORLD_ID));
    }
}
