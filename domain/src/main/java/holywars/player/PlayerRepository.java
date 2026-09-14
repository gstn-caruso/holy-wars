package holywars.player;

import java.util.Optional;

public interface PlayerRepository {

    void save(Player player);

    Optional<Player> find(PlayerId id);

    Optional<Player> findHuman();
}
