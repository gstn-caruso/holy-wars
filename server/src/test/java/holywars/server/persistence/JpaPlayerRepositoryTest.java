package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JpaPlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savedPlayerIsFoundBackEqualToTheOriginal() {
        Player player = Player.human(new PlayerId(1), "Jugador", 500);

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
        playerRepository.save(Player.ai(new PlayerId(1), "Sparta", 500));
        Player human = Player.human(new PlayerId(2), "Jugador", 500);
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
