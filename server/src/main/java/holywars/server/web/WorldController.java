package holywars.server.web;

import holywars.player.Player;
import holywars.player.PlayerRepository;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.IslandPlot;
import holywars.world.World;
import holywars.world.WorldRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
class WorldController {

    private final WorldRepository worldRepository;
    private final TownRepository townRepository;
    private final PlayerRepository playerRepository;
    private final CapitalHeaders capitalHeaders;

    WorldController(WorldRepository worldRepository, TownRepository townRepository,
            PlayerRepository playerRepository, CapitalHeaders capitalHeaders) {
        this.worldRepository = worldRepository;
        this.townRepository = townRepository;
        this.playerRepository = playerRepository;
        this.capitalHeaders = capitalHeaders;
    }

    @GetMapping("/map")
    String map(Model model) {
        World world = findWorldOrThrow();
        return playerRepository.find()
                .flatMap(player -> capitalHeaders.forPlayer(world, player))
                .map(header -> showMap(model, world, header))
                .orElse("redirect:/");
    }

    @GetMapping("/islands/{id}")
    String island(@PathVariable("id") long id, Model model) {
        World world = findWorldOrThrow();
        Island island = world.island(new IslandId(id));
        Optional<IslandPlot> occupiedPlot = occupiedPlot(island);
        String occupiedTownName = occupiedPlot.map(this::townNameOf).orElse(null);
        return playerRepository.find()
                .flatMap(player -> capitalHeaders.forPlayer(world, player)
                        .map(header -> showIsland(model, island, occupiedTownName, occupiedPlot, player, header)))
                .orElse("redirect:/");
    }

    private String showMap(Model model, World world, CapitalHeaderView header) {
        model.addAttribute("rows", mapRows(world));
        model.addAttribute("header", header);
        model.addAttribute("breadcrumb", BreadcrumbView.worldOnly());
        return "map";
    }

    private String showIsland(Model model, Island island, String occupiedTownName, Optional<IslandPlot> occupiedPlot,
            Player player, CapitalHeaderView header) {
        String ownerName = occupiedPlot.map(plot -> player.name()).orElse(null);
        model.addAttribute("island", IslandView.of(island, occupiedTownName, ownerName));
        model.addAttribute("header", header);
        model.addAttribute("breadcrumb", BreadcrumbView.upToIsland(island));
        return "island";
    }

    private Optional<IslandPlot> occupiedPlot(Island island) {
        return island.occupiedPlots().stream().findFirst();
    }

    private String townNameOf(IslandPlot plot) {
        long townId = plot.occupant().orElseThrow();
        return townRepository.find(new TownId(townId)).orElseThrow().name();
    }

    private World findWorldOrThrow() {
        return worldRepository.find().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private List<List<MapCellView>> mapRows(World world) {
        MapCellView[][] grid = new MapCellView[World.GRID_SIZE][World.GRID_SIZE];
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
