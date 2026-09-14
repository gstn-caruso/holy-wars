package holywars.resources;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class ElapsedTest {

    @Test
    void sinceRejectsGoingBackwards() {
        Instant previous = Instant.parse("2026-01-01T01:00:00Z");
        Instant now = Instant.parse("2026-01-01T00:00:00Z");

        assertThatThrownBy(() -> Elapsed.since(previous, now))
                .isInstanceOf(InvalidAdvanceInstantException.class);
    }
}
