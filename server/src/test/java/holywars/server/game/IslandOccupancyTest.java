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
import jakarta.persistence.EntityManager;
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
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500));
        townRepository.save(new Town(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3)));
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
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500));
        playerRepository.save(Player.ai(new PlayerId(2), "Rival", 500));
        townRepository.save(new Town(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3)));
        townRepository.save(new Town(new TownId(2), "Corinto", new PlayerId(2), new PlotLocation(new IslandId(1), 7)));
        entityManager.flush();
        entityManager.clear();

        Map<Integer, IslandOccupancy.Occupant> occupancy = islandOccupancy.of(new IslandId(1));

        assertThat(occupancy).containsOnlyKeys(3, 7);
        assertThat(occupancy.get(3).ownerName()).isEqualTo("Jugador");
        assertThat(occupancy.get(7).ownerName()).isEqualTo("Rival");
    }

    @Test
    void throwsWhenATownsOwnerCannotBeFound() {
        townRepository.save(new Town(new TownId(1), "Esparta", new PlayerId(99), new PlotLocation(new IslandId(1), 3)));
        entityManager.flush();
        entityManager.clear();

        assertThatThrownBy(() -> islandOccupancy.of(new IslandId(1)))
                .isInstanceOf(UnknownPlayerException.class);
    }
}
