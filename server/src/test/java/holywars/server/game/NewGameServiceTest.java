package holywars.server.game;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerKind;
import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownRepository;
import holywars.world.World;
import holywars.world.WorldRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NewGameServiceTest {

    @Autowired
    private NewGameService newGameService;

    @Autowired
    private WorldRepository worldRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void startPersistsTheWorldFourPlayersAndFourFoundedTowns() {
        newGameService.start(42L);
        entityManager.flush();
        entityManager.clear();

        World world = worldRepository.find().orElseThrow();
        long occupiedPlots = world.islands().stream()
                .flatMap(island -> island.plots().stream())
                .filter(plot -> !plot.isFree())
                .count();
        assertThat(occupiedPlots).isEqualTo(4);

        Player human = playerRepository.findHuman().orElseThrow();
        assertThat(human.id()).isEqualTo(new PlayerId(1));
        assertThat(human.kind()).isEqualTo(PlayerKind.HUMAN);

        List<Town> towns = IntStream.rangeClosed(1, 4)
                .mapToObj(PlayerId::new)
                .flatMap(id -> townRepository.findByOwner(id).stream())
                .toList();
        assertThat(towns).hasSize(4);
        assertThat(towns).extracting(town -> town.location().island()).doesNotHaveDuplicates();
    }

    @Test
    void aSecondStartWithADifferentSeedDoesNotChangeTheGame() {
        newGameService.start(42L);
        entityManager.flush();
        entityManager.clear();
        World firstWorld = worldRepository.find().orElseThrow();
        List<Town> firstTowns = townRepository.findByOwner(new PlayerId(1));

        newGameService.start(7L);
        entityManager.flush();
        entityManager.clear();

        assertThat(worldRepository.find()).contains(firstWorld);
        assertThat(townRepository.findByOwner(new PlayerId(1))).isEqualTo(firstTowns);
    }
}
