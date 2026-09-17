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
class ShellChromeAssetsTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({
        "ui-slot-left.svg, 53, 40",
        "ui-gift.svg, 53, 40",
        "ui-menu-troops.svg, 34, 34",
        "ui-menu-resource-shop.svg, 34, 34",
        "ui-menu-trader.svg, 34, 34",
        "ui-menu-rearrange.svg, 34, 34",
        "ui-menu-friends.svg, 34, 34",
        "ui-menu-info.svg, 34, 34",
        "ui-friends-panel.svg, 42, 295",
        "ui-slot-right.svg, 53, 40",
        "ui-button-edit.svg, 35, 13",
        "ui-button-showhide.svg, 35, 13",
        "ui-button-pagedown.svg, 35, 13"
    })
    void servesTheShellChromeSvgsAtTheirExactDimensions(String fileName, String width, String height)
            throws Exception {
        mockMvc.perform(get("/img/" + fileName))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("viewBox=\"0 0 " + width + " " + height + "\"")))
                .andExpect(content().string(containsString("width=\"" + width + "\"")))
                .andExpect(content().string(containsString("height=\"" + height + "\"")));
    }
}
