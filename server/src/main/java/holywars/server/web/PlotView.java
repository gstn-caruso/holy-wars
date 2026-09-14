package holywars.server.web;

record PlotView(int number, boolean occupied, Integer townId, String townName, String ownerName) {

    static PlotView free(int number) {
        return new PlotView(number, false, null, null, null);
    }

    static PlotView occupiedBy(int number, int townId, String townName, String ownerName) {
        return new PlotView(number, true, townId, townName, ownerName);
    }
}
