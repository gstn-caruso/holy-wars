package holywars.server.game;

import holywars.game.NewGame;
import holywars.player.PlayerRepository;
import holywars.town.TownRepository;
import holywars.world.WorldGenerator;
import holywars.world.WorldRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GameConfiguration {

    @Bean
    WorldGenerator worldGenerator() {
        return new WorldGenerator();
    }

    @Bean
    NewGame newGame(WorldRepository worldRepository, PlayerRepository playerRepository,
            TownRepository townRepository, WorldGenerator worldGenerator) {
        return new NewGame(worldRepository, playerRepository, townRepository, worldGenerator);
    }
}
