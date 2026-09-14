package holywars.server.web;

class IncompleteTownSceneLayoutException extends RuntimeException {

    IncompleteTownSceneLayoutException(int missingPosition) {
        super("Town scene layout is missing plot position " + missingPosition);
    }
}
