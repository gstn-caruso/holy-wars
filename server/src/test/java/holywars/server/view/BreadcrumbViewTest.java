package holywars.server.view;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class BreadcrumbViewTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void worldOnlyHasJustTheWorldCrumb() {
        BreadcrumbView breadcrumb = BreadcrumbView.worldOnly();

        assertThat(breadcrumb.crumbs()).hasSize(1);
        assertThat(breadcrumb.crumbs().get(0).label()).isEqualTo("Mundo");
        assertThat(breadcrumb.crumbs().get(0).href()).isEqualTo("/map");
    }

    @Test
    void upToIslandAddsTheIslandWithItsCoordinate() {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 4), "Naxos", LuxuryResource.WINE);

        BreadcrumbView breadcrumb = BreadcrumbView.upToIsland(island);

        assertThat(breadcrumb.crumbs()).hasSize(2);
        assertThat(breadcrumb.crumbs().get(1).label()).isEqualTo("Naxos [2:4]");
        assertThat(breadcrumb.crumbs().get(1).href()).isEqualTo("/islands/3");
    }

    @Test
    void upToTownAddsTheTownAfterTheIsland() {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 4), "Naxos", LuxuryResource.WINE);
        Town town = Town.founded(new TownId(1), new PlayerId(1), island.id(), 1, "Atenas", LuxuryResource.WINE, NOW);

        BreadcrumbView breadcrumb = BreadcrumbView.upToTown(island, town);

        assertThat(breadcrumb.crumbs()).hasSize(3);
        assertThat(breadcrumb.crumbs().get(2).label()).isEqualTo("Atenas");
        assertThat(breadcrumb.crumbs().get(2).href()).isEqualTo("/towns/1");
    }

    @Test
    void formatsTheOriginCoordinateInsteadOfLeavingItBlank() {
        Island island = Island.withFreePlots(new IslandId(9), new Coordinate(0, 0), "Delos", LuxuryResource.MARBLE);

        BreadcrumbView breadcrumb = BreadcrumbView.upToIsland(island);

        assertThat(breadcrumb.crumbs().get(1).label()).isEqualTo("Delos [0:0]");
    }
}
