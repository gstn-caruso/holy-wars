package holywars.game;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.Island;
import holywars.world.IslandPlot;
import holywars.world.World;
import holywars.world.WorldGenerator;
import holywars.world.WorldRepository;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public final class NewGame {

    private static final List<String> GREEK_TOWN_NAMES = List.of(
            "Atenas", "Esparta", "Corinto", "Tebas", "Argos", "Micenas", "Delfos", "Olimpia",
            "Mileto", "Éfeso", "Rodas", "Cnosos", "Pilos", "Tirinto", "Megara", "Eleusis",
            "Maratón", "Platea", "Sición", "Epidauro", "Nemea", "Larisa", "Calcis", "Eretria");

    private final WorldRepository worldRepository;
    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;
    private final WorldGenerator worldGenerator;

    public NewGame(WorldRepository worldRepository, PlayerRepository playerRepository,
            TownRepository townRepository, WorldGenerator worldGenerator) {
        this.worldRepository = worldRepository;
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
        this.worldGenerator = worldGenerator;
    }

    public void start(long seed, Instant now) {
        if (worldRepository.find().isPresent()) {
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

        worldRepository.save(world);
        playerRepository.save(player);
        townRepository.save(capital);
    }
}
