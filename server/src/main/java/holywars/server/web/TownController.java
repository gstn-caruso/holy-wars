package holywars.server.web;

import holywars.player.Player;
import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.Island;
import holywars.world.World;
import holywars.world.WorldRepository;
import java.time.Clock;
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
    private final Clock clock;

    TownController(TownRepository townRepository, WorldRepository worldRepository,
            PlayerRepository playerRepository, TownSceneProperties townSceneLayout, Clock clock) {
        this.townRepository = townRepository;
        this.worldRepository = worldRepository;
        this.playerRepository = playerRepository;
        this.townSceneLayout = townSceneLayout;
        this.clock = clock;
    }

    @GetMapping("/towns/{id}")
    String town(@PathVariable("id") long id, Model model) {
        Town town = townRepository.find(new TownId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Player player = playerRepository.find().orElseThrow();
        World world = worldRepository.find().orElseThrow();
        Island island = world.island(town.islandId());

        model.addAttribute("town", TownView.of(town, player.name(), island.name()));
        model.addAttribute("scene", TownSceneView.of(town, townSceneLayout));
        model.addAttribute("header", capitalHeader(world, player));
        model.addAttribute("breadcrumb", BreadcrumbView.upToTown(island, town));
        return "town";
    }

    private CapitalHeaderView capitalHeader(World world, Player player) {
        Town capital = townRepository.findByOwner(player.id()).orElseThrow();
        Island capitalIsland = world.island(capital.islandId());
        ResourceBarView resourceBar = ResourceBarView.of(capital, player, clock.instant());
        return CapitalHeaderView.of(capitalIsland, capital, resourceBar);
    }
}
