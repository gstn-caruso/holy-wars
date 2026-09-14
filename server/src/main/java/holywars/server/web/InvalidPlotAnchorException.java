package holywars.server.web;

class InvalidPlotAnchorException extends RuntimeException {

    InvalidPlotAnchorException(String text) {
        super("Plot anchor must have the form 'cx,cy,width', was '" + text + "'");
    }
}
