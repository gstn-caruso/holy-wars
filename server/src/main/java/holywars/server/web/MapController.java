package holywars.server.web;

import holywars.world.Coordinate;
import holywars.world.GridSize;
import holywars.world.World;
import holywars.world.WorldRepository;
import java.util.List;
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
        GridSize grid = world.grid();
        return IntStream.range(0, grid.height())
                .mapToObj(y -> IntStream.range(0, grid.width())
                        .mapToObj(x -> cellAt(world, x, y))
                        .toList())
                .toList();
    }

    private MapCellView cellAt(World world, int x, int y) {
        return world.islandAt(new Coordinate(x, y))
                .map(MapCellView::islandCell)
                .orElseGet(MapCellView::seaCell);
    }
}
