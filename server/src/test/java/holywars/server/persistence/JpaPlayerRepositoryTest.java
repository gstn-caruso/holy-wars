package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JpaPlayerRepositoryTest {

    private static final Instant STARTED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savedPlayerIsFoundBackEqualToTheOriginal() {
        Player player = Player.human(new PlayerId(1), "Jugador", 500, STARTED_AT);

        playerRepository.save(player);
        entityManager.flush();
        entityManager.clear();

        assertThat(playerRepository.find(new PlayerId(1))).contains(player);
    }

    @Test
    void savedPlayerWithAdvancedGoldIsFoundBackEqualToTheOriginal() {
        Player player = Player.human(new PlayerId(1), "Jugador", 500, STARTED_AT)
                .advancedTo(STARTED_AT.plus(Duration.ofHours(1)));

        playerRepository.save(player);
        entityManager.flush();
        entityManager.clear();

        assertThat(playerRepository.find(new PlayerId(1))).contains(player);
    }

    @Test
    void findIsEmptyForAnUnknownPlayer() {
        assertThat(playerRepository.find(new PlayerId(99))).isEmpty();
    }

    @Test
    void findHumanReturnsTheHumanPlayer() {
        playerRepository.save(Player.ai(new PlayerId(1), "Sparta", 500, STARTED_AT));
        Player human = Player.human(new PlayerId(2), "Jugador", 500, STARTED_AT);
        playerRepository.save(human);
        entityManager.flush();
        entityManager.clear();

        assertThat(playerRepository.findHuman()).contains(human);
    }

    @Test
    void findHumanIsEmptyWhenThereIsNoHumanYet() {
        assertThat(playerRepository.findHuman()).isEmpty();
    }
}
