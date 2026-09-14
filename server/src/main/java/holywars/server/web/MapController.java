package holywars.server.web;

import holywars.world.Coordinate;
import holywars.world.GridSize;
import holywars.world.Island;
import holywars.world.World;
import holywars.world.WorldRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
class MapController {

    private final WorldRepository worldRepository;

    MapController(WorldRepository worldRepository) {
        this.worldRepository = worldRepository;
    }

    @GetMapping("/map")
    String map(Model model) {
        World world = worldRepository.find()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("rows", rows(world));
        return "map";
    }

    private List<List<MapCellView>> rows(World world) {
        Map<Coordinate, Island> islandsByCoordinate = world.islands().stream()
                .collect(Collectors.toMap(Island::coordinate, Function.identity()));
        GridSize grid = world.grid();
        return IntStream.range(0, grid.height())
                .mapToObj(y -> IntStream.range(0, grid.width())
                        .mapToObj(x -> cellAt(islandsByCoordinate, x, y))
                        .toList())
                .toList();
    }

    private MapCellView cellAt(Map<Coordinate, Island> islandsByCoordinate, int x, int y) {
        Island island = islandsByCoordinate.get(new Coordinate(x, y));
        return island != null ? MapCellView.islandCell(island) : MapCellView.seaCell();
    }
}
