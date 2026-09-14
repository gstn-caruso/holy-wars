package holywars.server.web;

record MapCellView(boolean island, long islandId, String islandName, String iconPath, String villageCountLabel) {

    static MapCellView sea() {
        return new MapCellView(false, 0, null, null, null);
    }
}
