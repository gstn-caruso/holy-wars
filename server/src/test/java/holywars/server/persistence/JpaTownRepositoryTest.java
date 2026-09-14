package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JpaTownRepositoryTest {

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savedTownIsFoundBackEqualToTheOriginal() {
        Town town = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3));

        townRepository.save(town);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.find(new TownId(1))).contains(town);
    }

    @Test
    void findIsEmptyForAnUnknownTown() {
        assertThat(townRepository.find(new TownId(99))).isEmpty();
    }

    @Test
    void findByIslandReturnsTheTownsFoundedOnThatIsland() {
        Town onIslandOne = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3));
        Town alsoOnIslandOne = Town.founded(new TownId(2), "Atenas", new PlayerId(2), new PlotLocation(new IslandId(1), 5));
        Town onIslandTwo = Town.founded(new TownId(3), "Tebas", new PlayerId(3), new PlotLocation(new IslandId(2), 1));
        townRepository.save(onIslandOne);
        townRepository.save(alsoOnIslandOne);
        townRepository.save(onIslandTwo);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.findByIsland(new IslandId(1))).containsExactlyInAnyOrder(onIslandOne, alsoOnIslandOne);
    }

    @Test
    void findByIslandIsEmptyWhenThereAreNoTownsThere() {
        assertThat(townRepository.findByIsland(new IslandId(1))).isEmpty();
    }

    @Test
    void findByOwnerReturnsTheTownsFoundedByThatPlayer() {
        Town first = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3));
        Town second = Town.founded(new TownId(2), "Corinto", new PlayerId(1), new PlotLocation(new IslandId(2), 4));
        Town other = Town.founded(new TownId(3), "Tebas", new PlayerId(2), new PlotLocation(new IslandId(3), 1));
        townRepository.save(first);
        townRepository.save(second);
        townRepository.save(other);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.findByOwner(new PlayerId(1))).containsExactlyInAnyOrder(first, second);
    }

    @Test
    void findByOwnerIsEmptyWhenThePlayerHasNoTowns() {
        assertThat(townRepository.findByOwner(new PlayerId(1))).isEmpty();
    }

    @Test
    void findByOwnerReturnsTownsOrderedById() {
        Town higherId = Town.founded(new TownId(2), "Corinto", new PlayerId(1), new PlotLocation(new IslandId(1), 3));
        Town lowerId = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(2), 4));
        townRepository.save(higherId);
        townRepository.save(lowerId);
        entityManager.flush();
        entityManager.clear();

        assertThat(townRepository.findByOwner(new PlayerId(1))).containsExactly(lowerId, higherId);
    }
}
