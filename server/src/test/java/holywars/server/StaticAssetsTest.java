package holywars.server;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @Test
    void advisorPlusSvgIsServedWithItsFixedDimensions() throws Exception {
        mockMvc.perform(get("/img/ui-advisor-plus.svg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("viewBox=\"0 0 22 22\"")))
                .andExpect(content().string(containsString("width=\"22\"")))
                .andExpect(content().string(containsString("height=\"22\"")));
    }

    @ParameterizedTest
    @CsvSource({
        "ui-window-header.svg, 728, 26",
        "ui-window-footer.svg, 728, 5",
        "ui-window-close.svg, 18, 18",
        "ui-window-tab-flag.svg, 40, 40",
        "ui-box-header.svg, 680, 30",
        "ui-box-footer.svg, 680, 3",
        "ui-row-column.svg, 138, 1",
        "ui-button-build.svg, 1, 30",
        "ui-icon-time.svg, 20, 20"
    })
    void servesTheBuildWindowChromeAtItsExactDimensions(String fileName, String width, String height)
            throws Exception {
        mockMvc.perform(get("/img/" + fileName))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("width=\"" + width + "\"")))
                .andExpect(content().string(containsString("height=\"" + height + "\"")));
    }
}
