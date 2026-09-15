package holywars.server.player.repository;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.Players;
import holywars.server.player.entity.JpaPlayer;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaPlayers implements Players {

    private final JpaPlayerTable jpaPlayerTable;

    JpaPlayers(JpaPlayerTable jpaPlayerTable) {
        this.jpaPlayerTable = jpaPlayerTable;
    }

    @Override
    public Optional<Player> find() {
        return jpaPlayerTable.findAll().stream()
                .findFirst()
                .map(jpaPlayer -> Player.reconstituted(new PlayerId(jpaPlayer.id()), jpaPlayer.name(),
                        jpaPlayer.goldTicks(), jpaPlayer.goldUpdatedAt()));
    }

    @Override
    public void save(Player player) {
        jpaPlayerTable.save(new JpaPlayer(player.id().value(), player.name(), player.gold().ticks(),
                player.lastUpdate()));
    }
}
