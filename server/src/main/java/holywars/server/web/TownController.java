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
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
class TownController {

    private final TownRepository townRepository;
    private final PlayerRepository playerRepository;
    private final WorldRepository worldRepository;
    private final Clock clock;
    private final TownSceneAssembler townSceneAssembler;
    private final TownSceneProperties townSceneProperties;

    TownController(
            TownRepository townRepository,
            PlayerRepository playerRepository,
            WorldRepository worldRepository,
            Clock clock,
            TownSceneAssembler townSceneAssembler,
            TownSceneProperties townSceneProperties) {
        this.townRepository = townRepository;
        this.playerRepository = playerRepository;
        this.worldRepository = worldRepository;
        this.clock = clock;
        this.townSceneAssembler = townSceneAssembler;
        this.townSceneProperties = townSceneProperties;
    }

    @GetMapping("/towns/{id}")
    String town(@PathVariable("id") int id, Model model) {
        Town town = townRepository.find(new TownId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Player owner = playerRepository.find(town.ownerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        World world = worldRepository.find()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Island island = world.island(town.location().island())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("town", new TownView(
                town.name(),
                owner.name(),
                island.name(),
                island.id().value(),
                town.location().plotNumber(),
                townSceneProperties.width(),
                townSceneProperties.height(),
                townSceneAssembler.assemble(town)));
        model.addAttribute("resourceBar", resourceBarAdvancedToNow(town, owner));
        return "town";
    }

    private ResourceBarView resourceBarAdvancedToNow(Town town, Player owner) {
        Instant now = clock.instant();
        Town advancedTown = town.advancedTo(now);
        Player advancedOwner = owner.advancedTo(now);
        LuxuryResourceView luxury = LuxuryResourceView.of(advancedTown.resources().luxury());

        return new ResourceBarView(
                advancedTown.resources().wood(),
                advancedTown.resources().luxuryAmount(),
                luxury.spanishName(),
                luxury.icon(),
                advancedOwner.gold());
    }
}
