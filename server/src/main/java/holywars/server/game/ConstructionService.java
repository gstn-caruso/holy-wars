package holywars.server.game;

import holywars.town.BuildingType;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import java.time.Clock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConstructionService {

    private final TownRepository townRepository;
    private final Clock clock;

    public ConstructionService(TownRepository townRepository, Clock clock) {
        this.townRepository = townRepository;
        this.clock = clock;
    }

    @Transactional
    public Town start(TownId townId, int position, BuildingType type) {
        Town town = townRepository.find(townId).orElseThrow(() -> new UnknownTownException(townId));
        Town updated = town.startingConstruction(position, type, clock.instant());
        townRepository.save(updated);
        return updated;
    }
}
