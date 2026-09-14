package holywars.game;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerKind;
import holywars.town.Town;
import holywars.world.Coordinate;
import holywars.world.GridSize;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import holywars.world.World;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class GameSetupTest {

    @Test
    void startWithZeroAiPlayersCreatesOnlyTheHumanAndTheirCapital() {
        World world = worldWithIslands(3);
        GameSetupSettings settings = new GameSetupSettings(0, 500);

        NewGame newGame = GameSetup.start(world, new Random(42), settings);

        assertThat(newGame.players()).hasSize(1);
        Player human = newGame.players().get(0);
        assertThat(human.kind()).isEqualTo(PlayerKind.HUMAN);
        assertThat(human.name()).isEqualTo("Jugador");
        assertThat(human.gold()).isEqualTo(500);

        assertThat(newGame.towns()).hasSize(1);
        Town capital = newGame.towns().get(0);
        assertThat(capital.ownerId()).isEqualTo(human.id());
    }

    @Test
    void startWithOneAiPlayerCreatesTwoDistinctPlayersOnDistinctIslands() {
        World world = worldWithIslands(3);
        GameSetupSettings settings = new GameSetupSettings(1, 500);

        NewGame newGame = GameSetup.start(world, new Random(42), settings);

        assertThat(newGame.players()).hasSize(2);
        assertThat(newGame.players()).extracting(Player::kind)
                .containsExactlyInAnyOrder(PlayerKind.HUMAN, PlayerKind.AI);
        assertThat(newGame.players()).extracting(Player::id).doesNotHaveDuplicates();

        assertThat(newGame.towns()).hasSize(2);
        assertThat(newGame.towns()).extracting(town -> town.location().island()).doesNotHaveDuplicates();
    }

    private World worldWithIslands(int count) {
        List<Island> islands = java.util.stream.IntStream.rangeClosed(1, count)
                .mapToObj(index -> Island.withFreePlots(
                        new IslandId(index), new Coordinate(index, index), "Island" + index, LuxuryResource.WINE))
                .toList();
        return new World(new GridSize(10, 10), islands);
    }
}
