package holywars.game;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.Players;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import holywars.world.Island;
import holywars.world.IslandPlot;
import holywars.world.World;
import holywars.world.WorldGenerator;
import holywars.world.Worlds;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public final class NewGame {

    private static final List<String> GREEK_TOWN_NAMES = List.of(
            "Atenas", "Esparta", "Corinto", "Tebas", "Argos", "Micenas", "Delfos", "Olimpia",
            "Mileto", "Éfeso", "Rodas", "Cnosos", "Pilos", "Tirinto", "Megara", "Eleusis",
            "Maratón", "Platea", "Sición", "Epidauro", "Nemea", "Larisa", "Calcis", "Eretria");

    private final Worlds worlds;
    private final Players players;
    private final Towns towns;
    private final WorldGenerator worldGenerator;

    public NewGame(Worlds worlds, Players players,
            Towns towns, WorldGenerator worldGenerator) {
        this.worlds = worlds;
        this.players = players;
        this.towns = towns;
        this.worldGenerator = worldGenerator;
    }

    public void start(long seed, Instant now) {
        if (worlds.find().isPresent()) {
            return;
        }

        Random random = new Random(seed);
        World world = worldGenerator.generate(random);
        Island capitalIsland = world.randomIsland(random);
        IslandPlot capitalPlot = capitalIsland.firstFreePlot();

        Player player = Player.starting(new PlayerId(1), "Jugador", now);
        TownId townId = new TownId(1);
        capitalPlot.occupy(townId.value());
        String townName = GREEK_TOWN_NAMES.get(random.nextInt(GREEK_TOWN_NAMES.size()));
        Town capital = Town.founded(townId, player.id(), capitalIsland.id(), capitalPlot.number(), townName,
                capitalIsland.luxuryResource(), now);

        worlds.save(world);
        players.save(player);
        towns.save(capital);
    }
}
