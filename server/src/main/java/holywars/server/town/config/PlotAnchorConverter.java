package holywars.server.town.config;

import holywars.server.town.errors.InvalidPlotAnchorException;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
class PlotAnchorConverter implements Converter<String, PlotAnchor> {

    @Override
    public PlotAnchor convert(String raw) {
        String[] parts = raw.split(",");
        if (parts.length != 3) {
            throw new InvalidPlotAnchorException(raw);
        }
        try {
            return new PlotAnchor(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        } catch (NumberFormatException e) {
            throw new InvalidPlotAnchorException(raw);
        }
    }
}
