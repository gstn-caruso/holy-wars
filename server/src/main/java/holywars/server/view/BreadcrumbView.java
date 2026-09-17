package holywars.server.view;

import holywars.town.Town;
import holywars.world.Island;
import java.util.List;

public record BreadcrumbView(List<Crumb> crumbs) {

    record CrumbIcon(String path, int width, int height) {
    }

    record Crumb(String label, String href, CrumbIcon icon, boolean current) {
    }

    private static final CrumbIcon WORLD_ICON = new CrumbIcon("/img/breadcrumb-world.svg", 20, 20);
    private static final CrumbIcon ISLAND_ICON = new CrumbIcon("/img/breadcrumb-island.svg", 32, 20);

    private static final Crumb WORLD_CRUMB = new Crumb("Mundo", "/map", WORLD_ICON, false);

    public static BreadcrumbView worldOnly() {
        return new BreadcrumbView(List.of(WORLD_CRUMB));
    }

    public static BreadcrumbView upToIsland(Island island) {
        return new BreadcrumbView(List.of(WORLD_CRUMB, islandCrumb(island)));
    }

    public static BreadcrumbView upToTown(Island island, Town town) {
        return new BreadcrumbView(List.of(WORLD_CRUMB, islandCrumb(island), townCrumb(town)));
    }

    private static Crumb islandCrumb(Island island) {
        String label = island.name() + " " + island.coordinate().label();
        return new Crumb(label, "/islands/" + island.id().value(), ISLAND_ICON, false);
    }

    private static Crumb townCrumb(Town town) {
        return new Crumb(town.name(), "/towns/" + town.id().value(), null, true);
    }
}
