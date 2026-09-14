package holywars.game;

import holywars.player.Player;
import holywars.town.Town;
import holywars.world.World;
import java.util.List;

public record NewGame(World world, List<Player> players, List<Town> towns) {

    public NewGame {
        players = List.copyOf(players);
        towns = List.copyOf(towns);
    }
}
