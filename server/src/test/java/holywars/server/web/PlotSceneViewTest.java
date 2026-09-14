package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.town.BuildingSlot;
import holywars.town.BuildingSlotKind;
import org.junit.jupiter.api.Test;

class PlotSceneViewTest {

    @Test
    void occupiedSlotShowsTheTownHallSpriteAndLevel() {
        BuildingSlot slot = new BuildingSlot(1, BuildingSlotKind.TOWN_HALL, 1, BuildingSlotKind.TOWN_HALL, 1);
        PlotAnchor anchor = new PlotAnchor(600, 330, 140);

        PlotSceneView view = PlotSceneView.of(slot, 1, anchor);

        assertThat(view.spriteHref()).isEqualTo("/img/building-town-hall.svg");
        assertThat(view.label()).isEqualTo("Ayuntamiento nivel 1");
    }

    @Test
    void freeSlotShowsTheFreePlotSprite() {
        BuildingSlot slot = new BuildingSlot(2, BuildingSlotKind.LAND, 1);
        PlotAnchor anchor = new PlotAnchor(600, 180, 140);

        PlotSceneView view = PlotSceneView.of(slot, 1, anchor);

        assertThat(view.spriteHref()).isEqualTo("/img/plot-free.svg");
        assertThat(view.label()).isEqualTo("Parcela libre");
    }
}
