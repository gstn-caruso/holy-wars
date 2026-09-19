package holywars.server.config;

import holywars.UseCase;
import holywars.wiring.WiringCheck;
import holywars.wiring.WiringCheckRequest;
import holywars.wiring.WiringCheckResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DomainUseCasesConfigurationTest {

    @Autowired
    private UseCase<WiringCheckRequest, WiringCheckResponse> wiringCheck;

    @Test
    void registersWiringCheckAsABeanWithoutAnnotationsInDomain() {
        assertThat(wiringCheck).isInstanceOf(WiringCheck.class);
    }
}
