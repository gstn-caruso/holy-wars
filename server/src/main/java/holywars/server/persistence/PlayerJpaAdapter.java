package holywars.server.persistence;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class PlayerJpaAdapter implements PlayerRepository {

    private final PlayerJpaRepository playerJpaRepository;

    PlayerJpaAdapter(PlayerJpaRepository playerJpaRepository) {
        this.playerJpaRepository = playerJpaRepository;
    }

    @Override
    public Optional<Player> find() {
        return playerJpaRepository.findAll().stream()
                .findFirst()
                .map(entity -> new Player(new PlayerId(entity.id()), entity.name()));
    }

    @Override
    public void save(Player player) {
        playerJpaRepository.save(new PlayerEntity(player.id().value(), player.name()));
    }
}
