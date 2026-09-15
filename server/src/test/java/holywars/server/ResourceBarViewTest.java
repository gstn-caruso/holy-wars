package holywars.server;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ResourceBarViewTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void readsWoodLuxuryAndGoldAdvancedToNow() {
        Town capital = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        Instant anHourLater = NOW.plus(Duration.ofHours(1));

        ResourceBarView bar = ResourceBarView.of(capital, player, anHourLater);

        assertThat(bar.wood()).isEqualTo(530);
        assertThat(bar.luxuryAmount()).isEqualTo(110);
        assertThat(bar.gold()).isEqualTo(520);
    }

    @Test
    void exposesTheSpanishNameAndIconOfTheCapitalsLuxuryResource() {
        Town capital = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.MARBLE, NOW);
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);

        ResourceBarView bar = ResourceBarView.of(capital, player, NOW);

        assertThat(bar.luxuryResourceName()).isEqualTo("Mármol");
        assertThat(bar.luxuryIconPath()).isEqualTo("/img/resource-marble.svg");
    }
}
