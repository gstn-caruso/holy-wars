package holywars.server.web;

record PlotView(int number, boolean occupied, String townName, String ownerName, long townId) {

    static PlotView free(int number) {
        return new PlotView(number, false, null, null, 0);
    }
}
