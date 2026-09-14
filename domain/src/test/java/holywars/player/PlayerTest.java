package holywars.player;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void humanFactoryBuildsAPlayerOfKindHuman() {
        Player player = Player.human(new PlayerId(1), "Jugador", 500);

        assertThat(player.id()).isEqualTo(new PlayerId(1));
        assertThat(player.name()).isEqualTo("Jugador");
        assertThat(player.kind()).isEqualTo(PlayerKind.HUMAN);
        assertThat(player.gold()).isEqualTo(500);
    }

    @Test
    void aiFactoryBuildsAPlayerOfKindAi() {
        Player player = Player.ai(new PlayerId(2), "Perseo", 500);

        assertThat(player.id()).isEqualTo(new PlayerId(2));
        assertThat(player.name()).isEqualTo("Perseo");
        assertThat(player.kind()).isEqualTo(PlayerKind.AI);
        assertThat(player.gold()).isEqualTo(500);
    }
}
