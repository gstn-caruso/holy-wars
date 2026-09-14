package holywars.server.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.player.UnknownPlayerException;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class IslandOccupancyTest {

    private static final Instant FOUNDED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private IslandOccupancy islandOccupancy;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void isEmptyForAnIslandWithoutTowns() {
        assertThat(islandOccupancy.of(new IslandId(1))).isEmpty();
    }

    @Test
    void mapsAFoundedTownToItsPlotWithTheOwnerName() {
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500, FOUNDED_AT));
        townRepository.save(Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT));
        entityManager.flush();
        entityManager.clear();

        Map<Integer, IslandOccupancy.Occupant> occupancy = islandOccupancy.of(new IslandId(1));

        assertThat(occupancy).containsOnlyKeys(3);
        IslandOccupancy.Occupant occupant = occupancy.get(3);
        assertThat(occupant.townId()).isEqualTo(new TownId(1));
        assertThat(occupant.townName()).isEqualTo("Esparta");
        assertThat(occupant.ownerName()).isEqualTo("Jugador");
    }

    @Test
    void mapsSeveralFoundedTownsOnTheSameIslandToTheirOwners() {
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500, FOUNDED_AT));
        playerRepository.save(Player.ai(new PlayerId(2), "Rival", 500, FOUNDED_AT));
        townRepository.save(Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT));
        townRepository.save(Town.founded(new TownId(2), "Corinto", new PlayerId(2), new PlotLocation(new IslandId(1), 7), LuxuryResource.WINE, FOUNDED_AT));
        entityManager.flush();
        entityManager.clear();

        Map<Integer, IslandOccupancy.Occupant> occupancy = islandOccupancy.of(new IslandId(1));

        assertThat(occupancy).containsOnlyKeys(3, 7);
        assertThat(occupancy.get(3).ownerName()).isEqualTo("Jugador");
        assertThat(occupancy.get(7).ownerName()).isEqualTo("Rival");
    }

    @Test
    void throwsWhenATownsOwnerCannotBeFound() {
        townRepository.save(Town.founded(new TownId(1), "Esparta", new PlayerId(99), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE, FOUNDED_AT));
        entityManager.flush();
        entityManager.clear();

        assertThatThrownBy(() -> islandOccupancy.of(new IslandId(1)))
                .isInstanceOf(UnknownPlayerException.class);
    }
}
