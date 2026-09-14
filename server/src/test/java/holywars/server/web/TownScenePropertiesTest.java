package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TownScenePropertiesTest {

    @Test
    void anchorForReturnsTheConfiguredAnchorOfAPosition() {
        TownSceneProperties properties = new TownSceneProperties(1200, 720, fullLayout());

        assertThat(properties.anchorFor(1)).isEqualTo(new PlotAnchor(600, 330, 140));
    }

    @Test
    void rejectsALayoutMissingAPosition() {
        Map<Integer, String> incompleteLayout = new HashMap<>(fullLayout());
        incompleteLayout.remove(14);

        assertThatThrownBy(() -> new TownSceneProperties(1200, 720, incompleteLayout))
                .isInstanceOf(IncompleteTownSceneLayoutException.class);
    }

    private static Map<Integer, String> fullLayout() {
        return Map.ofEntries(
                Map.entry(1, "600,330,140"),
                Map.entry(2, "600,180,140"),
                Map.entry(3, "520,520,140"),
                Map.entry(4, "680,520,140"),
                Map.entry(5, "400,250,140"),
                Map.entry(6, "800,250,140"),
                Map.entry(7, "380,420,140"),
                Map.entry(8, "820,420,140"),
                Map.entry(9, "715,118,140"),
                Map.entry(10, "300,330,140"),
                Map.entry(11, "900,330,140"),
                Map.entry(12, "600,117,164"),
                Map.entry(13, "250,560,140"),
                Map.entry(14, "400,640,140"));
    }
}
