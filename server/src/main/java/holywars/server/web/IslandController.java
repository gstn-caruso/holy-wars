package holywars.server.web;

import holywars.server.game.IslandOccupancy;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.World;
import holywars.world.WorldRepository;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
class IslandController {

    private final WorldRepository worldRepository;
    private final IslandOccupancy islandOccupancy;

    IslandController(WorldRepository worldRepository, IslandOccupancy islandOccupancy) {
        this.worldRepository = worldRepository;
        this.islandOccupancy = islandOccupancy;
    }

    @GetMapping("/islands/{id}")
    String island(@PathVariable("id") int id, Model model) {
        World world = worldRepository.find()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Island island = world.island(new IslandId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LuxuryResourceView luxury = LuxuryResourceView.of(island.resource());
        model.addAttribute("island", island);
        model.addAttribute("luxuryName", luxury.spanishName());
        model.addAttribute("luxuryIcon", luxury.icon());
        model.addAttribute("plots", plotViews(island));
        return "island";
    }

    private List<PlotView> plotViews(Island island) {
        Map<Integer, IslandOccupancy.Occupant> occupancy = islandOccupancy.of(island.id());
        return island.plots().stream()
                .map(plot -> plotViewFor(plot.number(), occupancy.get(plot.number())))
                .toList();
    }

    private PlotView plotViewFor(int plotNumber, IslandOccupancy.Occupant occupant) {
        return occupant == null
                ? PlotView.free(plotNumber)
                : PlotView.occupiedBy(plotNumber, occupant.townId().value(), occupant.townName(), occupant.ownerName());
    }
}
