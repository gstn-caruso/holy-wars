package holywars.server.wiring.controller;

import holywars.UseCase;
import holywars.wiring.WiringCheckRequest;
import holywars.wiring.WiringCheckResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class WiringCheckController {

    private final UseCase<WiringCheckRequest, WiringCheckResponse> wiringCheck;

    WiringCheckController(UseCase<WiringCheckRequest, WiringCheckResponse> wiringCheck) {
        this.wiringCheck = wiringCheck;
    }

    @GetMapping("/api/wiring-check")
    WiringCheckResponse wiringCheck() {
        return wiringCheck.run(new WiringCheckRequest());
    }
}
