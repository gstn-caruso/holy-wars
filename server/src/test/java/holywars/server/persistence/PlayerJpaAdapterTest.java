package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class PlayerJpaAdapterTest {

    @Autowired
    private PlayerJpaRepository playerJpaRepository;

    private PlayerJpaAdapter playerJpaAdapter;

    @BeforeEach
    void setUp() {
        playerJpaAdapter = new PlayerJpaAdapter(playerJpaRepository);
    }

    @Test
    void savesAPlayerAndFindsItBackEqual() {
        Player player = new Player(new PlayerId(1), "Jugador");

        playerJpaAdapter.save(player);

        assertThat(playerJpaAdapter.find()).contains(player);
    }
}
