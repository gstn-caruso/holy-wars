package holywars.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
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
}
