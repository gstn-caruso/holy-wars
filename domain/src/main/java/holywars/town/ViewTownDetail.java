package holywars.town;

import holywars.UseCase;
import holywars.world.Island;
import holywars.world.Islands;

import java.util.List;

public class ViewTownDetail implements UseCase<ViewTownDetailRequest, ViewTownDetailResponse> {

    private final Towns towns;
    private final Islands islands;

    public ViewTownDetail(Towns towns, Islands islands) {
        this.towns = towns;
        this.islands = islands;
    }

    @Override
    public ViewTownDetailResponse run(ViewTownDetailRequest input) {
        Town town = towns.getById(input.townId());
        Island island = islands.findById(town.islandId()).orElseThrow();

        return new ViewTownDetailResponse(
                town.name(),
                island.name(),
                island.coordinates(),
                island.specialResource().resource(),
                town.resourceStock().amountsByResource(),
                viewPlotsOf(town));
    }

    private static List<TownPlotView> viewPlotsOf(Town town) {
        return town.plots().stream()
                .map(plot -> new TownPlotView())
                .toList();
    }
}
