package holywars.server.web;

import holywars.world.LuxuryResource;

final class LuxuryResourceIcon {

    private LuxuryResourceIcon() {
    }

    static String pathFor(LuxuryResource resource) {
        return "/img/resource-" + resource.name().toLowerCase() + ".svg";
    }
}
