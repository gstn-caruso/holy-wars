package holywars.server.town;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ConstructionWebFlowTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void emptyTheSharedDatabase() {
        jdbcTemplate.update("delete from town_plot");
        jdbcTemplate.update("delete from town");
        jdbcTemplate.update("delete from player");
        jdbcTemplate.update("delete from island_plot");
        jdbcTemplate.update("delete from island");
    }

    @Test
    void startingAConstructionShowsUpInTheBuildMenuTheBuildResultAndTheTownPage() throws Exception {
        mockMvc.perform(post("/world")).andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/towns/1/plots/2/build-menu"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Almacén")));

        mockMvc.perform(post("/towns/1/plots/2/build").param("type", "WAREHOUSE"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("En obra")))
                .andExpect(content().string(containsString("hx-swap-oob=\"true\"")));

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("En obra: Almacén")))
                .andExpect(content().string(containsString("460")));
    }

    @Test
    void buildingOnAnInvalidPlotPositionShowsItDoesNotExistInsteadOfFailing() throws Exception {
        mockMvc.perform(post("/world")).andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/towns/1/plots/20/build").param("type", "WAREHOUSE"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("La parcela no existe")));
    }

    @TestConfiguration
    static class FixedClockConfiguration {

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(NOW, ZoneOffset.UTC);
        }
    }
}
