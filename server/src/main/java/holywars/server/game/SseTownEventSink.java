package holywars.server.game;

import java.io.IOException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

final class SseTownEventSink implements TownEventSink {

    private final SseEmitter emitter;

    SseTownEventSink(SseEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void send(String eventName) throws IOException {
        emitter.send(SseEmitter.event().name(eventName));
    }
}
