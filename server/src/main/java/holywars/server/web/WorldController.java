package holywars.server.web;

import holywars.player.PlayerRepository;
import holywars.town.TownRepository;
import holywars.world.Island;
import holywars.world.World;
import holywars.world.WorldRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
class WorldController {

    private static final int GRID_SIZE = 10;

    private final WorldRepository worldRepository;
    private final TownRepository townRepository;
    private final PlayerRepository playerRepository;

    WorldController(WorldRepository worldRepository, TownRepository townRepository,
            PlayerRepository playerRepository) {
        this.worldRepository = worldRepository;
        this.townRepository = townRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/map")
    String map(Model model) {
        World world = findWorldOrThrow();
        model.addAttribute("rows", mapRows(world));
        return "map";
    }

    private World findWorldOrThrow() {
        return worldRepository.find().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private List<List<MapCellView>> mapRows(World world) {
        MapCellView[][] grid = new MapCellView[GRID_SIZE][GRID_SIZE];
        for (MapCellView[] row : grid) {
            Arrays.fill(row, MapCellView.sea());
        }
        for (Island island : world.islands()) {
            grid[island.coordinate().y()][island.coordinate().x()] = MapCellView.of(island);
        }
        List<List<MapCellView>> rows = new ArrayList<>();
        for (MapCellView[] row : grid) {
            rows.add(List.of(row));
        }
        return rows;
    }
}
