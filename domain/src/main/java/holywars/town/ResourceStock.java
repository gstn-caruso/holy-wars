package holywars.town;

import holywars.world.Resource;

import java.util.EnumMap;
import java.util.Map;

public final class ResourceStock {

    private final Map<Resource, ResourceAmount> amounts;

    public ResourceStock(Map<Resource, ResourceAmount> amounts) {
        this.amounts = new EnumMap<>(Resource.class);
        for (Resource resource : Resource.values()) {
            this.amounts.put(resource, amounts.getOrDefault(resource, ResourceAmount.zero()));
        }
    }

    public static ResourceStock empty() {
        return new ResourceStock(Map.of());
    }

    public ResourceAmount amountOf(Resource resource) {
        return amounts.get(resource);
    }

    public Map<Resource, ResourceAmount> asMap() {
        return Map.copyOf(amounts);
    }
}
