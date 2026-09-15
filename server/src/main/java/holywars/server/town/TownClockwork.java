package holywars.server.town;

import holywars.town.Town;
import holywars.town.TownId;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class TownClockwork {

    private static final Logger LOG = LoggerFactory.getLogger(TownClockwork.class);
    private static final Duration TICK_INTERVAL = Duration.ofSeconds(10);
    private static final Duration SUBSCRIPTION_TIMEOUT = Duration.ofMinutes(30);

    private final ScheduledExecutorService scheduler;
    private final Clock clock;
    private final Map<TownId, List<TownEventSink>> sinksByTown = new ConcurrentHashMap<>();
    private final Map<TownId, ScheduledFuture<?>> finishTasksByTown = new ConcurrentHashMap<>();

    public TownClockwork(ScheduledExecutorService scheduler, Clock clock) {
        this.scheduler = scheduler;
        this.clock = clock;
        long tickMillis = TICK_INTERVAL.toMillis();
        scheduler.scheduleAtFixedRate(this::tickResources, tickMillis, tickMillis, TimeUnit.MILLISECONDS);
    }

    public SseEmitter subscribe(TownId townId) {
        SseEmitter emitter = new SseEmitter(SUBSCRIPTION_TIMEOUT.toMillis());
        TownEventSink sink = new SseTownEventSink(emitter);
        subscribeSink(townId, sink);
        emitter.onCompletion(() -> removeSink(townId, sink));
        emitter.onTimeout(() -> removeSink(townId, sink));
        emitter.onError(exception -> removeSink(townId, sink));
        return emitter;
    }

    public void scheduleFinish(Town town) {
        TownId townId = town.id();
        ScheduledFuture<?> previous = finishTasksByTown.remove(townId);
        if (previous != null) {
            previous.cancel(false);
        }
        town.nextFinishAt().ifPresent(finishesAt -> {
            Duration delay = nonNegative(Duration.between(clock.instant(), finishesAt));
            ScheduledFuture<?> task = scheduler.schedule(() -> onFinish(townId), delay.toMillis(),
                    TimeUnit.MILLISECONDS);
            finishTasksByTown.put(townId, task);
            LOG.info("Scheduled finish for town {} in {}", townId, delay);
        });
    }

    void subscribeSink(TownId townId, TownEventSink sink) {
        sinksByTown.computeIfAbsent(townId, id -> new CopyOnWriteArrayList<>()).add(sink);
    }

    int subscriberCount(TownId townId) {
        return sinksByTown.getOrDefault(townId, List.of()).size();
    }

    boolean isTracking(TownId townId) {
        return sinksByTown.containsKey(townId);
    }

    private void onFinish(TownId townId) {
        finishTasksByTown.remove(townId);
        emit(townId, "town");
        emit(townId, "resources");
    }

    private void tickResources() {
        sinksByTown.keySet().forEach(townId -> emit(townId, "resources"));
    }

    private void emit(TownId townId, String eventName) {
        List<TownEventSink> sinks = sinksByTown.get(townId);
        if (sinks == null || sinks.isEmpty()) {
            sinksByTown.remove(townId, sinks);
            return;
        }
        LOG.info("Emitting {} to town {}", eventName, townId);
        for (TownEventSink sink : sinks) {
            try {
                sink.send(eventName);
            } catch (IOException | RuntimeException exception) {
                sinks.remove(sink);
            }
        }
    }

    private void removeSink(TownId townId, TownEventSink sink) {
        List<TownEventSink> sinks = sinksByTown.get(townId);
        if (sinks != null) {
            sinks.remove(sink);
        }
    }

    private static Duration nonNegative(Duration duration) {
        return duration.isNegative() ? Duration.ZERO : duration;
    }
}
