package holywars.server.web;

final class InvalidPlotAnchorException extends RuntimeException {

    InvalidPlotAnchorException(String raw) {
        super("Plot anchor must be formatted as \"cx,cy,width\", got \"" + raw + "\"");
    }
}
