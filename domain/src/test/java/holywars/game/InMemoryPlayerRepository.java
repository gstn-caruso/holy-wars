package holywars.game;

import holywars.player.Player;
import holywars.player.PlayerRepository;
import java.util.Optional;

final class InMemoryPlayerRepository implements PlayerRepository {

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
