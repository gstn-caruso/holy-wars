package holywars.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerKind;
import holywars.town.Town;
import holywars.world.CityPlot;
import holywars.world.Coordinate;
import holywars.world.GridSize;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import holywars.world.World;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
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
        assertThat(human.id()).isEqualTo(new PlayerId(1));
        assertThat(human.name()).isEqualTo("Jugador");
        assertThat(human.gold()).isEqualTo(500);

        assertThat(newGame.towns()).hasSize(1);
        Town capital = newGame.towns().get(0);
        assertThat(capital.ownerId()).isEqualTo(human.id());
        assertThat(capital.location().plotNumber()).isEqualTo(1);
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

    @Test
    void startWithSeveralAiPlayersAssignsEachToADifferentIsland() {
        World world = worldWithIslands(5);
        GameSetupSettings settings = new GameSetupSettings(3, 500);

        NewGame newGame = GameSetup.start(world, new Random(42), settings);

        assertThat(newGame.players()).hasSize(4);
        assertThat(newGame.towns()).hasSize(4);
        assertThat(newGame.towns()).extracting(town -> town.location().island()).doesNotHaveDuplicates();
    }

    @Test
    void sameSeedProducesTheSameAssignment() {
        World world = worldWithIslands(5);
        GameSetupSettings settings = new GameSetupSettings(3, 500);

        NewGame first = GameSetup.start(world, new Random(42), settings);
        NewGame second = GameSetup.start(world, new Random(42), settings);

        assertThat(first).isEqualTo(second);
    }

    @Test
    void startRejectsWhenThereAreNotEnoughIslandsForAllPlayers() {
        World world = worldWithIslands(3);
        GameSetupSettings settings = new GameSetupSettings(5, 500);

        assertThatThrownBy(() -> GameSetup.start(world, new Random(42), settings))
                .isInstanceOf(NotEnoughIslandsForPlayersException.class);
    }

    @Test
    void startProducesAWorldWithEveryCapitalPlotOccupied() {
        World world = worldWithIslands(5);
        GameSetupSettings settings = GameSetupSettings.standard();

        NewGame newGame = GameSetup.start(world, new Random(42), settings);

        for (Town town : newGame.towns()) {
            Island island = newGame.world().island(town.location().island()).orElseThrow();
            CityPlot capitalPlot = island.plots().stream()
                    .filter(plot -> plot.number() == town.location().plotNumber())
                    .findFirst()
                    .orElseThrow();
            assertThat(capitalPlot.town()).isEqualTo(Optional.of(town.id()));
        }

        long occupiedPlots = newGame.world().islands().stream()
                .flatMap(island -> island.plots().stream())
                .filter(plot -> !plot.isFree())
                .count();
        assertThat(occupiedPlots).isEqualTo(newGame.towns().size());
    }

    @Test
    void everyFoundedTownStartsWithATownHallAtLevelOne() {
        World world = worldWithIslands(5);
        GameSetupSettings settings = new GameSetupSettings(3, 500);

        NewGame newGame = GameSetup.start(world, new Random(42), settings);

        assertThat(newGame.towns()).extracting(Town::townHallLevel).containsOnly(1);
    }

    private World worldWithIslands(int count) {
        List<Island> islands = IntStream.rangeClosed(1, count)
                .mapToObj(index -> Island.withFreePlots(
                        new IslandId(index), new Coordinate(index, index), "Island" + index, LuxuryResource.WINE))
                .toList();
        return new World(new GridSize(10, 10), islands);
    }
}
