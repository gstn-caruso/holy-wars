package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.BuildingType;
import holywars.town.PlotLocation;
import holywars.town.SlotKind;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class BuildMenuAssemblerTest {

    private static final Instant FOUNDED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void aFreshlyFoundedTownOffersTheAllowedTypesOnEachFreeSlot() {
        Town town = Town.founded(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE,
                FOUNDED_AT);
        BuildMenuAssembler assembler = new BuildMenuAssembler();

        List<BuildOptionView> options = assembler.assemble(town);

        assertThat(options).extracting(BuildOptionView::position).containsExactlyInAnyOrder(2, 3, 4, 12, 13, 14);

        List<BuildingType> landTypes = BuildingType.forKind(SlotKind.LAND);
        assertThat(optionAt(options, 2).choices()).extracting(BuildingChoiceView::type)
                .containsExactlyInAnyOrderElementsOf(landTypes);
        assertThat(optionAt(options, 3).choices()).extracting(BuildingChoiceView::type)
                .containsExactlyInAnyOrderElementsOf(landTypes);
        assertThat(optionAt(options, 4).choices()).extracting(BuildingChoiceView::type)
                .containsExactlyInAnyOrderElementsOf(landTypes);
        assertThat(optionAt(options, 12).choices()).extracting(BuildingChoiceView::type)
                .containsExactly(BuildingType.WALL);
        assertThat(optionAt(options, 13).choices()).extracting(BuildingChoiceView::type)
                .containsExactlyInAnyOrder(BuildingType.TRADING_PORT, BuildingType.SHIPYARD);
        assertThat(optionAt(options, 14).choices()).extracting(BuildingChoiceView::type)
                .containsExactlyInAnyOrder(BuildingType.TRADING_PORT, BuildingType.SHIPYARD);
    }

    @Test
    void aChoiceShowsNameCostAndMinutes() {
        Town town = Town.founded(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE,
                FOUNDED_AT);
        BuildMenuAssembler assembler = new BuildMenuAssembler();

        List<BuildOptionView> options = assembler.assemble(town);

        BuildingChoiceView academy = options.stream()
                .flatMap(option -> option.choices().stream())
                .filter(choice -> choice.type() == BuildingType.ACADEMY)
                .findFirst()
                .orElseThrow();
        assertThat(academy.name()).isEqualTo("Academia");
        assertThat(academy.wood()).isEqualTo(80);
        assertThat(academy.luxury()).isEqualTo(20);
        assertThat(academy.minutes()).isEqualTo(10);
    }

    private static BuildOptionView optionAt(List<BuildOptionView> options, int position) {
        return options.stream().filter(option -> option.position() == position).findFirst().orElseThrow();
    }
}
