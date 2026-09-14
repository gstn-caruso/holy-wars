package holywars.server.web;

record TownView(
        int id,
        String name,
        String ownerName,
        String islandName,
        int islandId,
        int plotNumber,
        TownSceneView scene) {
}
