package holywars.server.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SchedulerConfiguration {

    @Bean(destroyMethod = "shutdownNow")
    ScheduledExecutorService townScheduler() {
        ThreadFactory daemonThreadFactory = task -> {
            Thread thread = new Thread(task, "town-clockwork");
            thread.setDaemon(true);
            return thread;
        };
        return Executors.newSingleThreadScheduledExecutor(daemonThreadFactory);
    }
}
