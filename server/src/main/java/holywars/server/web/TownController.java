package holywars.server.web;

import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.Island;
import holywars.world.WorldRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
class TownController {

    private final TownRepository townRepository;
    private final WorldRepository worldRepository;
    private final PlayerRepository playerRepository;
    private final TownSceneProperties townSceneLayout;

    TownController(TownRepository townRepository, WorldRepository worldRepository,
            PlayerRepository playerRepository, TownSceneProperties townSceneLayout) {
        this.townRepository = townRepository;
        this.worldRepository = worldRepository;
        this.playerRepository = playerRepository;
        this.townSceneLayout = townSceneLayout;
    }

    @GetMapping("/towns/{id}")
    String town(@PathVariable("id") long id, Model model) {
        Town town = townRepository.find(new TownId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String ownerName = playerRepository.find().orElseThrow().name();
        Island island = worldRepository.find().orElseThrow().island(town.islandId());
        model.addAttribute("town", TownView.of(town, ownerName, island.name()));
        model.addAttribute("scene", TownSceneView.of(town, townSceneLayout));
        return "town";
    }
}
