package holywars.server.game;

import java.io.IOException;

interface TownEventSink {

    void send(String eventName) throws IOException;
}
