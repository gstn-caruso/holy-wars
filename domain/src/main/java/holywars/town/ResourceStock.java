package holywars.town;

import holywars.world.Resource;

import java.util.EnumMap;
import java.util.Map;

public record ResourceStock(Map<Resource, ResourceAmount> amounts) {

    public ResourceStock {
        Map<Resource, ResourceAmount> normalized = new EnumMap<>(Resource.class);
        for (Resource resource : Resource.values()) {
            normalized.put(resource, amounts.getOrDefault(resource, ResourceAmount.zero()));
        }
        amounts = Map.copyOf(normalized);
    }

    public static ResourceStock empty() {
        return new ResourceStock(Map.of());
    }

    public ResourceAmount amountOf(Resource resource) {
        return amounts.get(resource);
    }

    public Map<Resource, ResourceAmount> asMap() {
        return amounts;
    }
}
