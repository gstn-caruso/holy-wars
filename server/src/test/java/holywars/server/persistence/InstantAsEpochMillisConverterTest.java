package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class InstantAsEpochMillisConverterTest {

    private final InstantAsEpochMillisConverter converter = new InstantAsEpochMillisConverter();

    @Test
    void roundTripsALiteralInstantThroughEpochMillis() {
        Instant instant = Instant.parse("2026-03-10T12:00:00Z");

        Long millis = converter.convertToDatabaseColumn(instant);
        Instant roundTripped = converter.convertToEntityAttribute(millis);

        assertThat(roundTripped).isEqualTo(instant);
    }

    @Test
    void truncatesSubMillisecondPrecision() {
        Instant instant = Instant.parse("2026-03-10T12:00:00.123456789Z");

        Long millis = converter.convertToDatabaseColumn(instant);
        Instant roundTripped = converter.convertToEntityAttribute(millis);

        assertThat(roundTripped).isEqualTo(Instant.parse("2026-03-10T12:00:00.123Z"));
    }
}
