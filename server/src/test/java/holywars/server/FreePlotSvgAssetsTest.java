package holywars.server;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FreePlotSvgAssetsTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({
        "plot-free-coast.svg, 172, 140",
        "plot-free-wall.svg, 201, 111"
    })
    void freePlotCoastAndWallSvgsAreServedAtTheirFixedDimensions(String fileName, String width, String height)
            throws Exception {
        mockMvc.perform(get("/img/" + fileName))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("viewBox=\"0 0 " + width + " " + height + "\"")))
                .andExpect(content().string(containsString("width=\"" + width + "\"")))
                .andExpect(content().string(containsString("height=\"" + height + "\"")));
    }
}
