package holywars.server.web;

import java.util.List;

record TownView(
        String name,
        String ownerName,
        String islandName,
        int islandId,
        int plotNumber,
        int sceneWidth,
        int sceneHeight,
        List<PlotSpriteView> plots) {
}
