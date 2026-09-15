package holywars.server.town.config;

import holywars.server.town.errors.IncompleteTownSceneLayoutException;
import java.util.Map;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "holywars.town-scene")
public record TownSceneProperties(int width, int height, Map<Integer, PlotAnchor> plots) {

    private static final Set<Integer> REQUIRED_POSITIONS = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);

    public TownSceneProperties {
        if (plots == null) {
            throw new IncompleteTownSceneLayoutException(Set.of());
        }
        if (!plots.keySet().equals(REQUIRED_POSITIONS)) {
            throw new IncompleteTownSceneLayoutException(plots.keySet());
        }
        plots = Map.copyOf(plots);
    }

    public PlotAnchor anchorFor(int position) {
        return plots.get(position);
    }
}
