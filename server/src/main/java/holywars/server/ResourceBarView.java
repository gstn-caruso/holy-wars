package holywars.server;

import holywars.player.Player;
import holywars.resources.TownResources;
import holywars.town.Town;
import java.time.Instant;

public record ResourceBarView(long townId, int wood, int luxuryAmount, String luxuryResourceName,
        String luxuryIconPath, int gold) {

    public static ResourceBarView of(Town capital, Player player, Instant now) {
        TownResources resources = capital.resources().advancedTo(now);
        int goldAmount = player.advancedTo(now).goldAmount();
        return new ResourceBarView(capital.id().value(), resources.woodAmount(), resources.luxuryAmount(),
                resources.luxuryResource().spanishName(), LuxuryResourceIcon.pathFor(resources.luxuryResource()),
                goldAmount);
    }
}
