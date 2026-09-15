package holywars.server.game;

import holywars.game.NewGame;
import holywars.player.Players;
import holywars.town.Towns;
import holywars.world.WorldGenerator;
import holywars.world.Worlds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GameConfiguration {

    @Bean
    WorldGenerator worldGenerator() {
        return new WorldGenerator();
    }

    @Bean
    NewGame newGame(Worlds worlds, Players players,
            Towns towns, WorldGenerator worldGenerator) {
        return new NewGame(worlds, players, towns, worldGenerator);
    }
}
