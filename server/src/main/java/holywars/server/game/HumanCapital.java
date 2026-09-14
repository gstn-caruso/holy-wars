package holywars.server.game;

import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class HumanCapital {

    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;

    HumanCapital(PlayerRepository playerRepository, TownRepository townRepository) {
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
    }

    public Optional<Town> find() {
        return playerRepository.findHuman()
                .flatMap(human -> townRepository.findByOwner(human.id()).stream().findFirst());
    }
}
