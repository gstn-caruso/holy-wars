package holywars.server.game;

import holywars.player.Players;
import holywars.town.Town;
import holywars.town.Towns;
import java.util.Optional;
import java.util.Random;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class HomeController {

    private final Players players;
    private final Towns towns;
    private final NewGameService newGameService;

    HomeController(Players players, Towns towns,
            NewGameService newGameService) {
        this.players = players;
        this.towns = towns;
        this.newGameService = newGameService;
    }

    @GetMapping("/")
    String home() {
        Optional<Town> capital = players.find().flatMap(player -> towns.findByOwner(player.id()));
        return capital.map(town -> "redirect:/towns/" + town.id().value()).orElse("game/index");
    }

    @PostMapping("/world")
    String startNewGame(@RequestParam(name = "seed", required = false) Long seed) {
        newGameService.start(seed != null ? seed : new Random().nextLong());
        return "redirect:/";
    }
}
