package holywars.server;

import holywars.player.Player;
import holywars.town.Town;
import holywars.town.Towns;
import holywars.world.Island;
import holywars.world.World;
import java.time.Clock;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CapitalHeaders {

    private final Towns towns;
    private final Clock clock;

    CapitalHeaders(Towns towns, Clock clock) {
        this.towns = towns;
        this.clock = clock;
    }

    public Optional<CapitalHeaderView> forPlayer(World world, Player player) {
        return towns.findByOwner(player.id())
                .map(capital -> buildHeader(world, player, capital));
    }

    private CapitalHeaderView buildHeader(World world, Player player, Town capital) {
        Island capitalIsland = world.island(capital.islandId());
        ResourceBarView resourceBar = ResourceBarView.of(capital, player, clock.instant());
        return CapitalHeaderView.of(capitalIsland, capital, resourceBar);
    }
}
