package holywars.server.web;

import holywars.world.WorldGenerationSettings;
import holywars.world.WorldGenerator;
import holywars.world.WorldRepository;
import java.util.Random;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class WorldController {

    private final WorldRepository worldRepository;

    WorldController(WorldRepository worldRepository) {
        this.worldRepository = worldRepository;
    }

    @PostMapping("/world")
    String createWorld(@RequestParam(name = "seed", required = false) Long seed) {
        if (worldRepository.find().isEmpty()) {
            long actualSeed = seed != null ? seed : new Random().nextLong();
            worldRepository.save(WorldGenerator.generate(actualSeed, WorldGenerationSettings.standard()));
        }
        return "redirect:/map";
    }
}
