package holywars.server.town;

import java.io.IOException;

interface TownEventSink {

    void send(String eventName) throws IOException;
}
