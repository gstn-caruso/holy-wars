package holywars.server.web;

import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownRepository;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.World;
import holywars.world.WorldRepository;
import java.util.HashMap;
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
    private final TownRepository townRepository;
    private final PlayerRepository playerRepository;

    IslandController(WorldRepository worldRepository, TownRepository townRepository, PlayerRepository playerRepository) {
        this.worldRepository = worldRepository;
        this.townRepository = townRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/islands/{id}")
    String island(@PathVariable("id") int id, Model model) {
        World world = worldRepository.find()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Island island = world.island(new IslandId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("island", island);
        model.addAttribute("plots", plotViews(island));
        return "island";
    }

    private List<PlotView> plotViews(Island island) {
        List<Town> towns = townRepository.findByIsland(island.id());
        Map<Integer, Town> townsByPlot = new HashMap<>();
        towns.forEach(town -> townsByPlot.put(town.location().plotNumber(), town));

        Map<PlayerId, String> ownerNames = new HashMap<>();
        towns.forEach(town -> ownerNames.computeIfAbsent(town.ownerId(),
                ownerId -> playerRepository.find(ownerId).map(owner -> owner.name()).orElse("")));

        return island.plots().stream()
                .map(plot -> {
                    Town town = townsByPlot.get(plot.number());
                    return town == null
                            ? PlotView.free(plot.number())
                            : PlotView.occupiedBy(plot.number(), town.id().value(), town.name(), ownerNames.get(town.ownerId()));
                })
                .toList();
    }
}
