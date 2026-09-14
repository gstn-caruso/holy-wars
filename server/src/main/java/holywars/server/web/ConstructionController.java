package holywars.server.web;

import holywars.server.game.ConstructionService;
import holywars.server.game.UnknownTownException;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import java.time.Clock;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
class ConstructionController {

    private final ConstructionService constructionService;
    private final TownRepository townRepository;
    private final Clock clock;

    ConstructionController(ConstructionService constructionService, TownRepository townRepository, Clock clock) {
        this.constructionService = constructionService;
        this.townRepository = townRepository;
        this.clock = clock;
    }

    @GetMapping("/towns/{id}/slots/{position}/build-menu")
    String buildMenu(@PathVariable("id") long id, @PathVariable("position") int position, Model model) {
        Town town = findOrThrow(id);
        model.addAttribute("menu", BuildMenuView.of(town, position, clock.instant()));
        return "fragments/buildMenu :: buildMenu(menu=${menu})";
    }

    private Town findOrThrow(long id) {
        TownId townId = new TownId(id);
        return townRepository.find(townId).orElseThrow(() -> new UnknownTownException(townId));
    }
}
