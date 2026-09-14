package holywars.server.web;

import holywars.server.game.HumanCapital;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {

    private final HumanCapital humanCapital;

    HomeController(HumanCapital humanCapital) {
        this.humanCapital = humanCapital;
    }

    @GetMapping("/")
    String home() {
        return humanCapital.find()
                .map(town -> "redirect:/towns/" + town.id().value())
                .orElse("index");
    }
}
