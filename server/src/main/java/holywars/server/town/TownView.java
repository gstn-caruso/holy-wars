package holywars.server.town;

import holywars.town.Town;

record TownView(long id, String name, String ownerName, long islandId, String islandName, int plotNumber) {

    static TownView of(Town town, String ownerName, String islandName) {
        return new TownView(town.id().value(), town.name(), ownerName, town.islandId().value(), islandName,
                town.plotNumber());
    }
}
