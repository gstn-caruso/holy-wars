package holywars.server.web;

import holywars.player.Player;
import holywars.resources.TownResources;
import holywars.town.Town;
import java.time.Instant;

record ResourceBarView(int wood, int luxuryAmount, String luxuryResourceName, String luxuryIconPath, int gold) {

    static ResourceBarView of(Town capital, Player player, Instant now) {
        TownResources resources = capital.resources().advancedTo(now);
        int goldAmount = player.advancedTo(now).goldAmount();
        return new ResourceBarView(resources.woodAmount(), resources.luxuryAmount(),
                resources.luxuryResource().spanishName(), LuxuryResourceIcon.pathFor(resources.luxuryResource()),
                goldAmount);
    }
}
