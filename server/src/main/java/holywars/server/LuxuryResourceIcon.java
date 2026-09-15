package holywars.server;

import holywars.world.LuxuryResource;

public final class LuxuryResourceIcon {

    private LuxuryResourceIcon() {
    }

    public static String pathFor(LuxuryResource resource) {
        return "/img/resource-" + resource.name().toLowerCase() + ".svg";
    }
}
