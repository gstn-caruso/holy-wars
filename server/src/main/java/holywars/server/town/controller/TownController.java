package holywars.server.town.controller;

import holywars.player.Player;
import holywars.player.Players;
import holywars.server.town.config.TownSceneProperties;
import holywars.server.town.errors.UnknownTownException;
import holywars.server.town.view.TownSceneView;
import holywars.server.town.view.TownView;
import holywars.server.view.BreadcrumbView;
import holywars.server.view.CapitalHeaders;
import holywars.server.view.ResourceBarView;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import holywars.world.Island;
import holywars.world.World;
import holywars.world.Worlds;
import java.time.Clock;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
class TownController {

    private final Towns towns;
    private final Worlds worlds;
    private final Players players;
    private final TownSceneProperties townSceneLayout;
    private final CapitalHeaders capitalHeaders;
    private final Clock clock;

    TownController(Towns towns, Worlds worlds,
            Players players, TownSceneProperties townSceneLayout, CapitalHeaders capitalHeaders,
            Clock clock) {
        this.towns = towns;
        this.worlds = worlds;
        this.players = players;
        this.townSceneLayout = townSceneLayout;
        this.capitalHeaders = capitalHeaders;
        this.clock = clock;
    }

    @GetMapping("/towns/{id}")
    String town(@PathVariable("id") long id, Model model) {
        Town town = townOrThrow(id);
        Player player = players.find().orElseThrow();
        World world = worlds.find().orElseThrow();
        Island island = world.island(town.islandId());

        model.addAttribute("town", TownView.of(town, player.name(), island.name()));
        model.addAttribute("scene", TownSceneView.of(town, townSceneLayout, clock.instant()));
        model.addAttribute("header", capitalHeaders.forPlayer(world, player).orElseThrow());
        model.addAttribute("breadcrumb", BreadcrumbView.upToTown(island, town));
        return "town/town";
    }

    @GetMapping("/towns/{id}/scene")
    String scene(@PathVariable("id") long id, Model model) {
        Town town = townOrThrow(id);
        model.addAttribute("scene", TownSceneView.of(town, townSceneLayout, clock.instant()));
        return "town/fragments/townScene :: townScene(scene=${scene}, oob=false)";
    }

    @GetMapping("/towns/{id}/resources")
    String resources(@PathVariable("id") long id, Model model) {
        Town town = townOrThrow(id);
        Player player = players.find().orElseThrow();
        model.addAttribute("bar", ResourceBarView.of(town, player, clock.instant()));
        return "fragments/layout :: resourceBar(bar=${bar}, oob=false)";
    }

    private Town townOrThrow(long id) {
        TownId townId = new TownId(id);
        return towns.find(townId).orElseThrow(() -> new UnknownTownException(townId));
    }
}
