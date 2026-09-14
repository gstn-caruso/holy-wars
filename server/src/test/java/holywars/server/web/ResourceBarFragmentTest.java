package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

class ResourceBarFragmentTest {

    @Test
    void showsASpanishLabelForEachResource() {
        String html = renderResourceBar(new ResourceBarView(500, 100, "Vino", "resource-wine.svg", 500));

        assertThat(occurrencesOf(html, "class=\"resource-label\"")).isEqualTo(3);
        assertThat(html).contains("<span class=\"resource-label\">Madera</span>");
        assertThat(html).contains("<span class=\"resource-label\">Vino</span>");
        assertThat(html).contains("<span class=\"resource-label\">Oro</span>");
    }

    private long occurrencesOf(String text, String token) {
        return Pattern.compile(Pattern.quote(token)).matcher(text).results().count();
    }

    private String renderResourceBar(ResourceBarView bar) {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);

        JakartaServletWebApplication application =
                JakartaServletWebApplication.buildApplication(new MockServletContext());
        IWebExchange webExchange = application.buildExchange(
                new MockHttpServletRequest(), new MockHttpServletResponse());
        WebContext context = new WebContext(webExchange, Locale.forLanguageTag("es"));
        context.setVariable("bar", bar);

        return engine.process("fragments", Set.of("resourceBar"), context);
    }
}
