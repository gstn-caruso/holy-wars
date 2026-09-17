package holywars.server;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HeaderAssetsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void headerScrollSvgIsServedWithItsFixedDimensions() throws Exception {
        mockMvc.perform(get("/img/ui-header-scroll.svg"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("viewBox=\"0 0 652 125\"")))
                .andExpect(content().string(containsString("width=\"652\"")))
                .andExpect(content().string(containsString("height=\"125\"")));
    }

    @ParameterizedTest
    @CsvSource({
        "ui-city-select.svg, 177, 24",
        "ui-button-world.svg, 84, 54",
        "ui-button-island.svg, 84, 54",
        "ui-button-city.svg, 84, 58",
        "ui-shop-button.svg, 168, 68",
        "ui-chest.svg, 41, 80",
        "ui-button-gold.svg, 110, 29",
        "ui-breadcrumb-ribbon.svg, 27, 28",
        "breadcrumb-world.svg, 20, 20",
        "breadcrumb-island.svg, 32, 20"
    })
    void servesTheRemainingHeaderPlaceholderAssetsAtTheirExactDimensions(String fileName, String width,
            String height) throws Exception {
        mockMvc.perform(get("/img/" + fileName))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("viewBox=\"0 0 " + width + " " + height + "\"")))
                .andExpect(content().string(containsString("width=\"" + width + "\"")))
                .andExpect(content().string(containsString("height=\"" + height + "\"")));
    }
}
