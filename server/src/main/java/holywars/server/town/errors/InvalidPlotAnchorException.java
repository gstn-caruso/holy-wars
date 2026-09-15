package holywars.server.town.errors;

public final class InvalidPlotAnchorException extends RuntimeException {

    public InvalidPlotAnchorException(String raw) {
        super("Plot anchor must be formatted as \"cx,cy,width\", got \"" + raw + "\"");
    }
}
