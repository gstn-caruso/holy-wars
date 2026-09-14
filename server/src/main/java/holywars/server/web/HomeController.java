package holywars.server.web;

import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownRepository;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {

    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;

    HomeController(PlayerRepository playerRepository, TownRepository townRepository) {
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
    }

    @GetMapping("/")
    String home() {
        Optional<Town> capital = playerRepository.find().flatMap(player -> townRepository.findByOwner(player.id()));
        return capital.map(town -> "redirect:/towns/" + town.id().value()).orElse("index");
    }
}
