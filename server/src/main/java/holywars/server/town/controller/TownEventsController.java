package holywars.server.town.controller;

import holywars.server.town.errors.UnknownTownException;
import holywars.server.town.service.TownClockwork;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
class TownEventsController {

    private final Towns towns;
    private final TownClockwork clockwork;

    TownEventsController(Towns towns, TownClockwork clockwork) {
        this.towns = towns;
        this.clockwork = clockwork;
    }

    @GetMapping("/towns/{id}/events")
    SseEmitter events(@PathVariable("id") long id) {
        TownId townId = new TownId(id);
        Town town = towns.find(townId).orElseThrow(() -> new UnknownTownException(townId));
        SseEmitter emitter = clockwork.subscribe(townId);
        clockwork.scheduleFinish(town);
        return emitter;
    }
}
