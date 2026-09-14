package holywars.server.game;

import holywars.game.GameSetup;
import holywars.game.GameSetupSettings;
import holywars.game.NewGame;
import holywars.player.PlayerRepository;
import holywars.town.TownRepository;
import holywars.world.World;
import holywars.world.WorldGenerationSettings;
import holywars.world.WorldGenerator;
import holywars.world.WorldRepository;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewGameService {

    private final WorldRepository worldRepository;
    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;

    NewGameService(WorldRepository worldRepository, PlayerRepository playerRepository, TownRepository townRepository) {
        this.worldRepository = worldRepository;
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
    }

    @Transactional
    public void start(long seed) {
        if (worldRepository.find().isPresent()) {
            return;
        }
        Random random = new Random(seed);
        World world = WorldGenerator.generate(random, WorldGenerationSettings.standard());
        NewGame game = GameSetup.start(world, random, GameSetupSettings.standard());

        worldRepository.save(game.world());
        game.players().forEach(playerRepository::save);
        game.towns().forEach(townRepository::save);
    }
}
