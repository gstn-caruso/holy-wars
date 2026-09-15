package holywars.server.town.service;

import java.io.IOException;

interface TownEventSink {

    void send(String eventName) throws IOException;
}
