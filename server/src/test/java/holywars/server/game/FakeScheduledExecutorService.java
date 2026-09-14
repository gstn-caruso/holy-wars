package holywars.server.game;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public final class FakeScheduledExecutorService extends AbstractExecutorService implements ScheduledExecutorService {

    private final List<FakeScheduledTask> oneShotTasks = new ArrayList<>();
    private final List<Runnable> periodicTasks = new ArrayList<>();

    @Override
    public void execute(Runnable command) {
        command.run();
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        FakeScheduledTask task = new FakeScheduledTask(command, unit.toMillis(delay));
        oneShotTasks.add(task);
        return task.future;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        periodicTasks.add(command);
        return new FakeScheduledFuture();
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay,
            TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
    }

    @Override
    public List<Runnable> shutdownNow() {
        return List.of();
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        return true;
    }

    public void runNextOneShotTask() {
        FakeScheduledTask task = pendingOneShotTasks().findFirst()
                .orElseThrow(() -> new NoSuchElementException("No pending one-shot task"));
        oneShotTasks.remove(task);
        task.command.run();
    }

    public void runPeriodicTick() {
        periodicTasks.forEach(Runnable::run);
    }

    public int pendingOneShotTaskCount() {
        return (int) pendingOneShotTasks().count();
    }

    public long lastScheduledDelayMillis() {
        return oneShotTasks.get(oneShotTasks.size() - 1).delayMillis;
    }

    private Stream<FakeScheduledTask> pendingOneShotTasks() {
        return oneShotTasks.stream().filter(task -> !task.future.isCancelled());
    }

    private static final class FakeScheduledTask {

        private final Runnable command;
        private final long delayMillis;
        private final FakeScheduledFuture future = new FakeScheduledFuture();

        private FakeScheduledTask(Runnable command, long delayMillis) {
            this.command = command;
            this.delayMillis = delayMillis;
        }
    }

    private static final class FakeScheduledFuture implements ScheduledFuture<Object> {

        private boolean cancelled;

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            cancelled = true;
            return true;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public boolean isDone() {
            return cancelled;
        }

        @Override
        public Object get() {
            return null;
        }

        @Override
        public Object get(long timeout, TimeUnit unit) {
            return null;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return 0;
        }

        @Override
        public int compareTo(Delayed other) {
            return 0;
        }
    }
}
