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
}
