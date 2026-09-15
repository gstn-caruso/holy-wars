package holywars.server.game;

import holywars.town.BuildingType;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import java.time.Clock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConstructionService {

    private final Towns towns;
    private final Clock clock;
    private final TownClockwork clockwork;

    public ConstructionService(Towns towns, Clock clock, TownClockwork clockwork) {
        this.towns = towns;
        this.clock = clock;
        this.clockwork = clockwork;
    }

    @Transactional
    public Town start(TownId townId, int position, BuildingType type) {
        Town town = towns.find(townId).orElseThrow(() -> new UnknownTownException(townId));
        Town updated = town.startingConstruction(position, type, clock.instant());
        towns.save(updated);
        clockwork.scheduleFinish(updated);
        return updated;
    }
}
