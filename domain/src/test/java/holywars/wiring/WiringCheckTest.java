package holywars.wiring;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WiringCheckTest {

    @Test
    void respondsThatTheWiringWorks() {
        WiringCheckResponse response = new WiringCheck().run(new WiringCheckRequest());

        assertThat(response.status()).isEqualTo("ok");
    }
}
