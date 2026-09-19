package holywars.wiring;

import holywars.UseCase;

public class WiringCheck implements UseCase<WiringCheckRequest, WiringCheckResponse> {

    @Override
    public WiringCheckResponse run(WiringCheckRequest input) {
        return new WiringCheckResponse("ok");
    }
}
