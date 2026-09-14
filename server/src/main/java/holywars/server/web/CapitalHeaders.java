package holywars.server.web;

import holywars.player.Player;
import holywars.town.Town;
import holywars.town.TownRepository;
import holywars.world.Island;
import holywars.world.World;
import java.time.Clock;
import org.springframework.stereotype.Component;

@Component
class CapitalHeaders {

    private final TownRepository townRepository;
    private final Clock clock;

    CapitalHeaders(TownRepository townRepository, Clock clock) {
        this.townRepository = townRepository;
        this.clock = clock;
    }

    CapitalHeaderView forPlayer(World world, Player player) {
        Town capital = townRepository.findByOwner(player.id()).orElseThrow();
        Island capitalIsland = world.island(capital.islandId());
        ResourceBarView resourceBar = ResourceBarView.of(capital, player, clock.instant());
        return CapitalHeaderView.of(capitalIsland, capital, resourceBar);
    }
}
