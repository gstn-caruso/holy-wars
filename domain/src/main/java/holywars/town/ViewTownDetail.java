package holywars.town;

import holywars.UseCase;
import holywars.world.Island;
import holywars.world.Islands;
import holywars.world.Resource;

import java.util.Map;
import java.util.stream.Collectors;

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

        Map<Resource, Long> resourceStock = town.resourceStock().asMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().value()));

        return new ViewTownDetailResponse(
                town.name(),
                island.name(),
                island.coordinates(),
                island.specialResource().resource(),
                resourceStock);
    }
}
