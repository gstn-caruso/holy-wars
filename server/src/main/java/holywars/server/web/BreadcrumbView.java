package holywars.server.web;

import holywars.town.Town;
import holywars.world.Island;
import java.util.List;

record BreadcrumbView(List<Crumb> crumbs) {

    record Crumb(String label, String href) {
    }

    private static final Crumb WORLD_CRUMB = new Crumb("Mundo", "/map");

    static BreadcrumbView worldOnly() {
        return new BreadcrumbView(List.of(WORLD_CRUMB));
    }

    static BreadcrumbView upToIsland(Island island) {
        return new BreadcrumbView(List.of(WORLD_CRUMB, islandCrumb(island)));
    }

    static BreadcrumbView upToTown(Island island, Town town) {
        return new BreadcrumbView(List.of(WORLD_CRUMB, islandCrumb(island), townCrumb(town)));
    }

    private static Crumb islandCrumb(Island island) {
        String label = island.name() + " " + island.coordinate().label();
        return new Crumb(label, "/islands/" + island.id().value());
    }

    private static Crumb townCrumb(Town town) {
        return new Crumb(town.name(), "/towns/" + town.id().value());
    }
}
