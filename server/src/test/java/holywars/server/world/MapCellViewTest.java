package holywars.server.world;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import org.junit.jupiter.api.Test;

class MapCellViewTest {

    @Test
    void seaCellDoesNotLinkToAnyIsland() {
        MapCellView cell = MapCellView.sea();

        assertThat(cell.island()).isFalse();
    }

    @Test
    void islandCellWithoutOccupiedPlotsOmitsTheVillageCountLabel() {
        Island island = Island.withFreePlots(new IslandId(1), new Coordinate(2, 3), "Naxos", LuxuryResource.WINE);

        MapCellView cell = MapCellView.of(island);

        assertThat(cell.island()).isTrue();
        assertThat(cell.islandId()).isEqualTo(1L);
        assertThat(cell.islandName()).isEqualTo("Naxos");
        assertThat(cell.villageCountLabel()).isNull();
    }

    @Test
    void islandCellWithOneOccupiedPlotSaysOneVillage() {
        Island island = Island.withFreePlots(new IslandId(1), new Coordinate(2, 3), "Naxos", LuxuryResource.WINE);
        island.plots().get(0).occupy(99L);

        MapCellView cell = MapCellView.of(island);

        assertThat(cell.villageCountLabel()).isEqualTo("1 aldea");
    }

    @Test
    void islandCellWithTwoOccupiedPlotsSaysTwoVillages() {
        Island island = Island.withFreePlots(new IslandId(1), new Coordinate(2, 3), "Naxos", LuxuryResource.WINE);
        island.plots().get(0).occupy(99L);
        island.plots().get(1).occupy(100L);

        MapCellView cell = MapCellView.of(island);

        assertThat(cell.villageCountLabel()).isEqualTo("2 aldeas");
    }
}
