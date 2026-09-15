package holywars.server.town;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.BuildingType;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class TownClockworkTest {

    private static final Instant FOUNDED_AT = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");
    private static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

    @Test
    void subscribeReturnsARegisteredEmitterAndScheduleFinishProgramsTheDelayUntilTheNearestFinish() {
        FakeScheduledExecutorService scheduler = new FakeScheduledExecutorService();
        TownClockwork clockwork = new TownClockwork(scheduler, CLOCK);
        TownId townId = new TownId(1);

        SseEmitter emitter = clockwork.subscribe(townId);

        assertThat(emitter).isNotNull();
        assertThat(clockwork.subscriberCount(townId)).isEqualTo(1);

        Town town = aTownWithAWarehouseFinishingIn(townId, Duration.ofMinutes(6));

        clockwork.scheduleFinish(town);

        assertThat(scheduler.lastScheduledDelayMillis()).isEqualTo(BuildingType.WAREHOUSE.buildTime().toMillis());
    }

    @Test
    void firingTheScheduledFinishTaskEmitsTownAndResourcesToAllSinksOfThatTown() {
        FakeScheduledExecutorService scheduler = new FakeScheduledExecutorService();
        TownClockwork clockwork = new TownClockwork(scheduler, CLOCK);
        TownId townId = new TownId(1);
        List<String> receivedByFirst = new ArrayList<>();
        List<String> receivedBySecond = new ArrayList<>();
        clockwork.subscribeSink(townId, receivedByFirst::add);
        clockwork.subscribeSink(townId, receivedBySecond::add);

        clockwork.scheduleFinish(aTownWithAWarehouseFinishingIn(townId, Duration.ofMinutes(6)));
        scheduler.runNextOneShotTask();

        assertThat(receivedByFirst).containsExactly("town", "resources");
        assertThat(receivedBySecond).containsExactly("town", "resources");
    }

    @Test
    void scheduleFinishWithoutConstructionsSchedulesNothingAndReschedulingCancelsThePreviousTask() {
        FakeScheduledExecutorService scheduler = new FakeScheduledExecutorService();
        TownClockwork clockwork = new TownClockwork(scheduler, CLOCK);
        TownId townId = new TownId(1);
        Town idleTown = Town.founded(townId, new PlayerId(1), new IslandId(1), 1, "Atenas", LuxuryResource.WINE,
                FOUNDED_AT);

        clockwork.scheduleFinish(idleTown);

        assertThat(scheduler.pendingOneShotTaskCount()).isZero();

        clockwork.scheduleFinish(aTownWithAWarehouseFinishingIn(townId, Duration.ofMinutes(10)));

        assertThat(scheduler.pendingOneShotTaskCount()).isEqualTo(1);

        clockwork.scheduleFinish(aTownWithAWarehouseFinishingIn(townId, Duration.ofMinutes(3)));

        assertThat(scheduler.pendingOneShotTaskCount()).isEqualTo(1);
        assertThat(scheduler.lastScheduledDelayMillis()).isEqualTo(Duration.ofMinutes(3).toMillis());
    }

    @Test
    void thePeriodicTickEmitsResourcesToTownsWithSinksAndStopsRetryingClosedOnes() {
        FakeScheduledExecutorService scheduler = new FakeScheduledExecutorService();
        TownClockwork clockwork = new TownClockwork(scheduler, CLOCK);
        TownId closedTown = new TownId(1);
        TownId openTown = new TownId(2);
        List<String> receivedByOpenTown = new ArrayList<>();
        clockwork.subscribeSink(closedTown, eventName -> {
            throw new IOException("closed");
        });
        clockwork.subscribeSink(openTown, receivedByOpenTown::add);

        scheduler.runPeriodicTick();
        scheduler.runPeriodicTick();

        assertThat(clockwork.isTracking(closedTown)).isFalse();
        assertThat(receivedByOpenTown).containsExactly("resources", "resources");
    }

    @Test
    void aSinkThatFailsToSendIsDiscardedWithoutAffectingOthersForTheSameTown() {
        FakeScheduledExecutorService scheduler = new FakeScheduledExecutorService();
        TownClockwork clockwork = new TownClockwork(scheduler, CLOCK);
        TownId townId = new TownId(1);
        List<String> receivedByHealthySink = new ArrayList<>();
        AtomicInteger failingSinkAttempts = new AtomicInteger();
        clockwork.subscribeSink(townId, eventName -> {
            failingSinkAttempts.incrementAndGet();
            throw new IOException("broken pipe");
        });
        clockwork.subscribeSink(townId, receivedByHealthySink::add);

        scheduler.runPeriodicTick();

        assertThat(receivedByHealthySink).containsExactly("resources");
        assertThat(failingSinkAttempts).hasValue(1);
        assertThat(clockwork.subscriberCount(townId)).isEqualTo(1);
    }

    @Test
    void aSinkThatThrowsAtRuntimeIsDiscardedAndTheTickKeepsRunning() {
        FakeScheduledExecutorService scheduler = new FakeScheduledExecutorService();
        TownClockwork clockwork = new TownClockwork(scheduler, CLOCK);
        TownId townId = new TownId(1);
        List<String> receivedByHealthySink = new ArrayList<>();
        clockwork.subscribeSink(townId, eventName -> {
            throw new IllegalStateException("completed");
        });
        clockwork.subscribeSink(townId, receivedByHealthySink::add);

        scheduler.runPeriodicTick();
        scheduler.runPeriodicTick();

        assertThat(receivedByHealthySink).containsExactly("resources", "resources");
    }

    private static Town aTownWithAWarehouseFinishingIn(TownId townId, Duration remaining) {
        Town founded = Town.founded(townId, new PlayerId(1), new IslandId(1), 1, "Atenas", LuxuryResource.WINE,
                FOUNDED_AT);
        Instant startedAt = NOW.minus(BuildingType.WAREHOUSE.buildTime()).plus(remaining);
        return founded.startingConstruction(2, BuildingType.WAREHOUSE, startedAt);
    }
}
