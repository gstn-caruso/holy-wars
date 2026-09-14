package holywars.server.web;

import holywars.player.Player;
import holywars.player.PlayerRepository;
import holywars.server.game.ConstructionService;
import holywars.server.game.UnknownTownException;
import holywars.town.BuildingType;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import java.time.Clock;
import java.time.Instant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class ConstructionController {

    private final ConstructionService constructionService;
    private final TownRepository townRepository;
    private final PlayerRepository playerRepository;
    private final TownSceneProperties townSceneLayout;
    private final Clock clock;

    ConstructionController(ConstructionService constructionService, TownRepository townRepository,
            PlayerRepository playerRepository, TownSceneProperties townSceneLayout, Clock clock) {
        this.constructionService = constructionService;
        this.townRepository = townRepository;
        this.playerRepository = playerRepository;
        this.townSceneLayout = townSceneLayout;
        this.clock = clock;
    }

    @GetMapping("/towns/{id}/slots/{position}/build-menu")
    String buildMenu(@PathVariable("id") long id, @PathVariable("position") int position, Model model) {
        Town town = findOrThrow(id);
        model.addAttribute("menu", BuildMenuView.of(town, position, clock.instant()));
        return "fragments/buildMenu :: buildMenu(menu=${menu})";
    }

    @PostMapping("/towns/{id}/slots/{position}/build")
    String build(@PathVariable("id") long id, @PathVariable("position") int position,
            @RequestParam("type") BuildingType type, Model model) {
        try {
            Town updated = constructionService.start(new TownId(id), position, type);
            Instant now = clock.instant();
            Player player = playerRepository.find().orElseThrow();
            model.addAttribute("menu", BuildMenuView.of(updated, position, now));
            model.addAttribute("scene", TownSceneView.of(updated, townSceneLayout, now));
            model.addAttribute("bar", ResourceBarView.of(updated, player, now));
            return "fragments/buildResult :: buildResult(menu=${menu},scene=${scene},bar=${bar})";
        } catch (RuntimeException exception) {
            Town town = findOrThrow(id);
            String error = ConstructionErrorMessages.forException(exception);
            model.addAttribute("menu", BuildMenuView.withError(town, position, clock.instant(), error));
            return "fragments/buildMenu :: buildMenu(menu=${menu})";
        }
    }

    private Town findOrThrow(long id) {
        TownId townId = new TownId(id);
        return townRepository.find(townId).orElseThrow(() -> new UnknownTownException(townId));
    }
}
