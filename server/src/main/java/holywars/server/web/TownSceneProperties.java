package holywars.server.web;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "holywars.town-scene")
record TownSceneProperties(int width, int height, Map<Integer, String> plots) {

    PlotAnchor anchorFor(int position) {
        return PlotAnchor.parse(plots.get(position));
    }
}
