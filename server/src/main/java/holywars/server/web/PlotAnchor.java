package holywars.server.web;

record PlotAnchor(int cx, int cy, int width) {

    static PlotAnchor parse(String text) {
        String[] values = text.split(",");
        if (values.length != 3) {
            throw new InvalidPlotAnchorException(text);
        }
        return new PlotAnchor(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
    }
}
