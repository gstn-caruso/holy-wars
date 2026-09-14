package holywars.player;

import java.util.Optional;

public interface PlayerRepository {

    Optional<Player> find();

    void save(Player player);
}
