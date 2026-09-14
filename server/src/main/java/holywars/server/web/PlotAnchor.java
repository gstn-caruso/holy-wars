package holywars.server.web;

record PlotAnchor(int cx, int cy, int width) {

    static PlotAnchor parse(String text) {
        String[] values = text.split(",");
        return new PlotAnchor(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
    }
}
