package holywars.server.town.errors;

import java.util.Set;

public final class IncompleteTownSceneLayoutException extends RuntimeException {

    public IncompleteTownSceneLayoutException(Set<Integer> positions) {
        super("Town scene layout must define plot anchors for positions 1 to 14, got " + positions);
    }
}
