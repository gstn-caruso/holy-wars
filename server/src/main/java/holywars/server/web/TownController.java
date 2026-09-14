package holywars.server.web;

import holywars.player.Player;
import holywars.player.PlayerRepository;
import holywars.server.game.UnknownTownException;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.Island;
import holywars.world.World;
import holywars.world.WorldRepository;
import java.time.Clock;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
class TownController {

    private final TownRepository townRepository;
    private final WorldRepository worldRepository;
    private final PlayerRepository playerRepository;
    private final TownSceneProperties townSceneLayout;
    private final CapitalHeaders capitalHeaders;
    private final Clock clock;

    TownController(TownRepository townRepository, WorldRepository worldRepository,
            PlayerRepository playerRepository, TownSceneProperties townSceneLayout, CapitalHeaders capitalHeaders,
            Clock clock) {
        this.townRepository = townRepository;
        this.worldRepository = worldRepository;
        this.playerRepository = playerRepository;
        this.townSceneLayout = townSceneLayout;
        this.capitalHeaders = capitalHeaders;
        this.clock = clock;
    }

    @GetMapping("/towns/{id}")
    String town(@PathVariable("id") long id, Model model) {
        Town town = townOrThrow(id);
        Player player = playerRepository.find().orElseThrow();
        World world = worldRepository.find().orElseThrow();
        Island island = world.island(town.islandId());

        model.addAttribute("town", TownView.of(town, player.name(), island.name()));
        model.addAttribute("scene", TownSceneView.of(town, townSceneLayout, clock.instant()));
        model.addAttribute("header", capitalHeaders.forPlayer(world, player).orElseThrow());
        model.addAttribute("breadcrumb", BreadcrumbView.upToTown(island, town));
        return "town";
    }

    @GetMapping("/towns/{id}/scene")
    String scene(@PathVariable("id") long id, Model model) {
        Town town = townOrThrow(id);
        model.addAttribute("scene", TownSceneView.of(town, townSceneLayout, clock.instant()));
        return "fragments/townScene :: townScene(scene=${scene}, oob=false)";
    }

    @GetMapping("/towns/{id}/resources")
    String resources(@PathVariable("id") long id, Model model) {
        Town town = townOrThrow(id);
        Player player = playerRepository.find().orElseThrow();
        model.addAttribute("bar", ResourceBarView.of(town, player, clock.instant()));
        return "fragments/layout :: resourceBar(bar=${bar}, oob=false)";
    }

    private Town townOrThrow(long id) {
        TownId townId = new TownId(id);
        return townRepository.find(townId).orElseThrow(() -> new UnknownTownException(townId));
    }
}
