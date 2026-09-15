package holywars.server.player;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.Players;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaPlayers implements Players {

    private final JpaPlayerTable playerJpaTable;

    JpaPlayers(JpaPlayerTable playerJpaTable) {
        this.playerJpaTable = playerJpaTable;
    }

    @Override
    public Optional<Player> find() {
        return playerJpaTable.findAll().stream()
                .findFirst()
                .map(entity -> Player.reconstituted(new PlayerId(entity.id()), entity.name(), entity.goldTicks(),
                        entity.goldUpdatedAt()));
    }

    @Override
    public void save(Player player) {
        playerJpaTable.save(new JpaPlayer(player.id().value(), player.name(), player.gold().ticks(),
                player.lastUpdate()));
    }
}
