package holywars.server;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StaticAssetsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void servesTheVendoredHtmxScript() throws Exception {
        mockMvc.perform(get("/js/htmx.min.js")).andExpect(status().isOk());
    }

    @Test
    void servesTheLiveUpdatesScript() throws Exception {
        mockMvc.perform(get("/js/holy-wars.js")).andExpect(status().isOk());
    }

    @Test
    void friezeFrameSvgIsServedWithItsFixedDimensions() throws Exception {
        mockMvc.perform(get("/img/ui-frieze-advisors.svg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("viewBox=\"0 0 394 130\"")))
                .andExpect(content().string(containsString("width=\"394\"")))
                .andExpect(content().string(containsString("height=\"130\"")));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "ui-advisor-cities.svg",
        "ui-advisor-military.svg",
        "ui-advisor-research.svg",
        "ui-advisor-diplomacy.svg"
    })
    void advisorPortraitSvgsAreServedWithTheirFixedDimensions(String fileName) throws Exception {
        mockMvc.perform(get("/img/" + fileName))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("viewBox=\"0 0 90 108\"")))
                .andExpect(content().string(containsString("width=\"90\"")))
                .andExpect(content().string(containsString("height=\"108\"")));
    }
}
