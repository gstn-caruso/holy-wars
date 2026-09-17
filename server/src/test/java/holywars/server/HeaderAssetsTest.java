package holywars.server;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
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
}
