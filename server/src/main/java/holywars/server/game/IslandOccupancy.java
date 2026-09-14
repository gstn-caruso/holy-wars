package holywars.server.game;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.player.UnknownPlayerException;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class IslandOccupancy {

    private final TownRepository townRepository;
    private final PlayerRepository playerRepository;

    IslandOccupancy(TownRepository townRepository, PlayerRepository playerRepository) {
        this.townRepository = townRepository;
        this.playerRepository = playerRepository;
    }

    public Map<Integer, Occupant> of(IslandId island) {
        List<Town> towns = townRepository.findByIsland(island);
        Map<PlayerId, String> ownerNames = ownerNamesOf(towns);

        Map<Integer, Occupant> occupancy = new HashMap<>();
        towns.forEach(town -> occupancy.put(town.location().plotNumber(),
                new Occupant(town.id(), town.name(), ownerNames.get(town.ownerId()))));
        return occupancy;
    }

    private Map<PlayerId, String> ownerNamesOf(List<Town> towns) {
        Map<PlayerId, String> names = new HashMap<>();
        towns.forEach(town -> names.computeIfAbsent(town.ownerId(), this::ownerNameOf));
        return names;
    }

    private String ownerNameOf(PlayerId ownerId) {
        return playerRepository.find(ownerId)
                .map(Player::name)
                .orElseThrow(() -> new UnknownPlayerException(ownerId));
    }

    public record Occupant(TownId townId, String townName, String ownerName) {
    }
}
