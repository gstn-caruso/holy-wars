package holywars.server.web;

record PlotView(int number, boolean occupied, String townName, String ownerName, long townId) {

    static PlotView free(int number) {
        return new PlotView(number, false, null, null, 0);
    }

    static PlotView occupied(int number, String townName, String ownerName, long townId) {
        return new PlotView(number, true, townName, ownerName, townId);
    }
}
