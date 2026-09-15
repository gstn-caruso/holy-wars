package holywars.player;

import java.util.Optional;

public interface Players {

    Optional<Player> find();

    void save(Player player);
}
