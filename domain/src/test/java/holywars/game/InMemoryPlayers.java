package holywars.game;

import holywars.player.Player;
import holywars.player.Players;
import java.util.Optional;

final class InMemoryPlayers implements Players {

    private Player player;
    private int saveCount;

    @Override
    public Optional<Player> find() {
        return Optional.ofNullable(player);
    }

    @Override
    public void save(Player player) {
        this.player = player;
        saveCount++;
    }

    int saveCount() {
        return saveCount;
    }
}
