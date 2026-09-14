package holywars.server.game;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerKind;
import holywars.player.PlayerRepository;
import holywars.server.MutableClock;
import holywars.server.TestClockConfiguration;
import holywars.town.Town;
import holywars.town.TownRepository;
import holywars.world.World;
import holywars.world.WorldRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(TestClockConfiguration.class)
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

    @Autowired
    private MutableClock clock;

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
    void startFoundsTownsAndPlayersAtTheClocksInstant() {
        Instant foundedAt = Instant.parse("2026-03-10T12:00:00Z");
        clock.set(foundedAt);

        newGameService.start(42L);
        entityManager.flush();
        entityManager.clear();

        List<Town> towns = IntStream.rangeClosed(1, 4)
                .mapToObj(PlayerId::new)
                .flatMap(id -> townRepository.findByOwner(id).stream())
                .toList();
        assertThat(towns).extracting(town -> town.resources().lastUpdate()).containsOnly(foundedAt);

        List<Player> players = IntStream.rangeClosed(1, 4)
                .mapToObj(PlayerId::new)
                .flatMap(id -> playerRepository.find(id).stream())
                .toList();
        assertThat(players).extracting(Player::lastUpdate).containsOnly(foundedAt);
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
