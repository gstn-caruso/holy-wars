package holywars.game;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerNames;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownNames;
import holywars.world.CityPlot;
import holywars.world.Island;
import holywars.world.World;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class GameSetup {

    private static final String HUMAN_NAME = "Jugador";

    private GameSetup() {
    }

    public static NewGame start(World world, Random random, GameSetupSettings settings, Instant startedAt) {
        int playerCount = 1 + settings.aiPlayers();
        List<Island> assignedIslands = pickDistinctIslands(world, playerCount, random);
        List<String> aiNames = PlayerNames.pick(settings.aiPlayers(), random);
        List<String> townNames = TownNames.pick(playerCount, random);

        List<Player> players = new ArrayList<>();
        List<Town> towns = new ArrayList<>();
        World updatedWorld = world;

        for (int index = 0; index < playerCount; index++) {
            PlayerId playerId = new PlayerId(index + 1);
            TownId townId = new TownId(index + 1);
            Player player = playerAt(index, playerId, aiNames, settings, startedAt);

            Island island = assignedIslands.get(index);
            CityPlot capitalPlot = island.firstFreePlot().orElseThrow();
            PlotLocation location = new PlotLocation(island.id(), capitalPlot.number());
            Town town = Town.founded(townId, townNames.get(index), playerId, location, island.resource(), startedAt);

            updatedWorld = updatedWorld.withCityFounded(location, townId);
            players.add(player);
            towns.add(town);
        }

        return new NewGame(updatedWorld, players, towns);
    }

    private static Player playerAt(
            int index, PlayerId playerId, List<String> aiNames, GameSetupSettings settings, Instant startedAt) {
        return index == 0
                ? Player.human(playerId, HUMAN_NAME, settings.startingGold(), startedAt)
                : Player.ai(playerId, aiNames.get(index - 1), settings.startingGold(), startedAt);
    }

    private static List<Island> pickDistinctIslands(World world, int playerCount, Random random) {
        if (world.islands().size() < playerCount) {
            throw new NotEnoughIslandsForPlayersException(playerCount, world.islands().size());
        }
        List<Island> shuffled = new ArrayList<>(world.islands());
        Collections.shuffle(shuffled, random);
        return List.copyOf(shuffled.subList(0, playerCount));
    }
}
