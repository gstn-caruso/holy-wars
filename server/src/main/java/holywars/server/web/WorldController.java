package holywars.server.web;

import holywars.player.PlayerRepository;
import holywars.town.TownRepository;
import holywars.world.World;
import holywars.world.WorldRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
class WorldController {

    private final WorldRepository worldRepository;
    private final TownRepository townRepository;
    private final PlayerRepository playerRepository;

    WorldController(WorldRepository worldRepository, TownRepository townRepository,
            PlayerRepository playerRepository) {
        this.worldRepository = worldRepository;
        this.townRepository = townRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/map")
    String map(Model model) {
        World world = findWorldOrThrow();
        return "map";
    }

    private World findWorldOrThrow() {
        return worldRepository.find().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
