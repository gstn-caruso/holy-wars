package holywars.server;

import java.time.Instant;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestClockConfiguration {

    @Bean
    @Primary
    MutableClock mutableClock() {
        return new MutableClock(Instant.EPOCH);
    }
}
