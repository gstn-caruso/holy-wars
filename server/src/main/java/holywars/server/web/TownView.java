package holywars.server.web;

record TownView(
        String name,
        String ownerName,
        String islandName,
        int islandId,
        int plotNumber,
        TownSceneView scene) {
}
