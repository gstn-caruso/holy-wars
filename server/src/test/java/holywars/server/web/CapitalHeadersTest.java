package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import holywars.world.World;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CapitalHeadersTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    private final TownRepository townRepository = mock(TownRepository.class);
    private final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
    private final CapitalHeaders capitalHeaders = new CapitalHeaders(townRepository, clock);

    @Test
    void buildsTheCapitalHeaderFromThePlayersOwnTownAtTheClocksInstant() {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        Town capital = Town.founded(new TownId(9), player.id(), island.id(), 1, "Atenas", LuxuryResource.WINE, NOW);
        given(townRepository.findByOwner(player.id())).willReturn(Optional.of(capital));

        CapitalHeaderView header = capitalHeaders.forPlayer(world, player);

        assertThat(header.capitalName()).isEqualTo("Atenas");
        assertThat(header.islandCoordinateLabel()).isEqualTo("[2:2]");
        assertThat(header.islandId()).isEqualTo(3L);
        assertThat(header.townId()).isEqualTo(9L);
        assertThat(header.resourceBar().wood()).isEqualTo(500);
        assertThat(header.resourceBar().gold()).isEqualTo(500);
    }
}
