package holywars.server.web;

import holywars.town.BuildingSlot;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "holywars.town-scene")
record TownSceneProperties(int width, int height, Map<Integer, String> plots) {

    TownSceneProperties {
        for (int position = 1; position <= BuildingSlot.HIGHEST_POSITION; position++) {
            if (!plots.containsKey(position)) {
                throw new IncompleteTownSceneLayoutException(position);
            }
            PlotAnchor.parse(plots.get(position));
        }
    }

    PlotAnchor anchorFor(int position) {
        return PlotAnchor.parse(plots.get(position));
    }
}
