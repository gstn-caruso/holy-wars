package holywars.server.web;

import holywars.server.game.NewGameService;
import java.util.Random;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class WorldController {

    private final NewGameService newGameService;

    WorldController(NewGameService newGameService) {
        this.newGameService = newGameService;
    }

    @PostMapping("/world")
    String createWorld(@RequestParam(name = "seed", required = false) Long seed) {
        newGameService.start(seed != null ? seed : new Random().nextLong());
        return "redirect:/";
    }
}
