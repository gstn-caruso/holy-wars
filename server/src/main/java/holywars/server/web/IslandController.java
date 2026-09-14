package holywars.server.web;

import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.World;
import holywars.world.WorldRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
class IslandController {

    private final WorldRepository worldRepository;

    IslandController(WorldRepository worldRepository) {
        this.worldRepository = worldRepository;
    }

    @GetMapping("/islands/{id}")
    String island(@PathVariable("id") int id, Model model) {
        World world = worldRepository.find()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Island island = world.island(new IslandId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("island", island);
        return "island";
    }
}
