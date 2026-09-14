package holywars.server.web;

import holywars.player.PlayerRepository;
import holywars.server.game.NewGameService;
import holywars.town.Town;
import holywars.town.TownRepository;
import java.util.Optional;
import java.util.Random;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class HomeController {

    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;
    private final NewGameService newGameService;

    HomeController(PlayerRepository playerRepository, TownRepository townRepository,
            NewGameService newGameService) {
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
        this.newGameService = newGameService;
    }

    @GetMapping("/")
    String home() {
        Optional<Town> capital = playerRepository.find().flatMap(player -> townRepository.findByOwner(player.id()));
        return capital.map(town -> "redirect:/towns/" + town.id().value()).orElse("index");
    }

    @PostMapping("/world")
    String startNewGame(@RequestParam(name = "seed", required = false) Long seed) {
        newGameService.start(seed != null ? seed : new Random().nextLong());
        return "redirect:/";
    }
}
