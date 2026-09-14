package holywars.server.web;

import holywars.world.WorldRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {

    private final WorldRepository worldRepository;

    HomeController(WorldRepository worldRepository) {
        this.worldRepository = worldRepository;
    }

    @GetMapping("/")
    String home() {
        return worldRepository.find().isPresent() ? "redirect:/map" : "index";
    }
}
