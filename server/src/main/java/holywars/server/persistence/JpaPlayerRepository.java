package holywars.server.persistence;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerKind;
import holywars.player.PlayerRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
class JpaPlayerRepository implements PlayerRepository {

    private final SpringDataPlayerRepository springDataPlayerRepository;

    JpaPlayerRepository(SpringDataPlayerRepository springDataPlayerRepository) {
        this.springDataPlayerRepository = springDataPlayerRepository;
    }

    @Override
    @Transactional
    public void save(Player player) {
        springDataPlayerRepository.save(PlayerEntityMapper.toEntity(player));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> find(PlayerId id) {
        return springDataPlayerRepository.findById(id.value()).map(PlayerEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> findHuman() {
        return springDataPlayerRepository.findFirstByKind(PlayerKind.HUMAN).map(PlayerEntityMapper::toDomain);
    }
}
