package holywars.server.town;

import java.util.Set;

final class IncompleteTownSceneLayoutException extends RuntimeException {

    IncompleteTownSceneLayoutException(Set<Integer> positions) {
        super("Town scene layout must define plot anchors for positions 1 to 14, got " + positions);
    }
}
