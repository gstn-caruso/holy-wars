package holywars.server.game;

import holywars.player.Player;
import holywars.player.PlayerRepository;
import holywars.town.BuildingType;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import java.time.Clock;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConstructionService {

    private final TownRepository townRepository;
    private final PlayerRepository playerRepository;
    private final Clock clock;

    ConstructionService(TownRepository townRepository, PlayerRepository playerRepository, Clock clock) {
        this.townRepository = townRepository;
        this.playerRepository = playerRepository;
        this.clock = clock;
    }

    @Transactional
    public void startConstruction(TownId townId, int position, BuildingType type) {
        Town town = townRepository.find(townId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Player owner = playerRepository.find(town.ownerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!owner.isHuman()) {
            throw new ForeignTownException(townId);
        }
        Town updatedTown = town.startingConstruction(position, type, clock.instant());
        townRepository.save(updatedTown);
    }
}
