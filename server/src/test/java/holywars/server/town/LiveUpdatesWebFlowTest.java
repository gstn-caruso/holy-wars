package holywars.server.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.town.BuildingType;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.ScheduledExecutorService;
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
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class LiveUpdatesWebFlowTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ScheduledExecutorService scheduler;

    @Autowired
    private Clock clock;

    @BeforeEach
    void emptyTheSharedDatabase() {
        jdbcTemplate.update("delete from town_plot");
        jdbcTemplate.update("delete from town");
        jdbcTemplate.update("delete from player");
        jdbcTemplate.update("delete from island_plot");
        jdbcTemplate.update("delete from island");
    }

    @Test
    void constructionFinishingNotifiesTheSubscribedStreamAndTheSceneReflectsTheCompletedBuilding() throws Exception {
        mockMvc.perform(post("/world")).andExpect(status().is3xxRedirection());

        MvcResult subscription = mockMvc.perform(get("/towns/1/events"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(post("/towns/1/plots/2/build").param("type", "WAREHOUSE")).andExpect(status().isOk());

        FakeScheduledExecutorService fakeScheduler = (FakeScheduledExecutorService) scheduler;
        fakeScheduler.runNextOneShotTask();

        assertThat(subscription.getResponse().getContentAsString()).contains("event:town");

        ((MutableClock) clock).advanceBy(BuildingType.WAREHOUSE.buildTime());

        mockMvc.perform(get("/towns/1/scene"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Almacén nivel 1")));
    }

    @TestConfiguration
    static class LiveUpdatesTestConfiguration {

        @Bean
        @Primary
        ScheduledExecutorService fakeScheduler() {
            return new FakeScheduledExecutorService();
        }

        @Bean
        @Primary
        Clock mutableClock() {
            return new MutableClock(NOW);
        }
    }

    private static final class MutableClock extends Clock {

        private Instant instant;

        MutableClock(Instant instant) {
            this.instant = instant;
        }

        void advanceBy(Duration duration) {
            instant = instant.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
