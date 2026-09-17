package holywars.server.view;

import holywars.town.Town;
import holywars.world.Island;
import java.util.List;

public record BreadcrumbView(List<Crumb> crumbs) {

    record Crumb(String label, String href, String iconPath, int iconWidth, int iconHeight) {
    }

    private static final Crumb WORLD_CRUMB = new Crumb("Mundo", "/map", "/img/breadcrumb-world.svg", 20, 20);

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
        return new Crumb(label, "/islands/" + island.id().value(), "/img/breadcrumb-island.svg", 32, 20);
    }

    private static Crumb townCrumb(Town town) {
        return new Crumb(town.name(), "/towns/" + town.id().value(), null, 0, 0);
    }
}
