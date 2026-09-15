package holywars.server.persistence;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.Players;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class PlayerJpaAdapter implements Players {

    private final PlayerJpaRepository playerJpaRepository;

    PlayerJpaAdapter(PlayerJpaRepository playerJpaRepository) {
        this.playerJpaRepository = playerJpaRepository;
    }

    @Override
    public Optional<Player> find() {
        return playerJpaRepository.findAll().stream()
                .findFirst()
                .map(entity -> Player.reconstituted(new PlayerId(entity.id()), entity.name(), entity.goldTicks(),
                        entity.goldUpdatedAt()));
    }

    @Override
    public void save(Player player) {
        playerJpaRepository.save(new PlayerEntity(player.id().value(), player.name(), player.gold().ticks(),
                player.lastUpdate()));
    }
}
