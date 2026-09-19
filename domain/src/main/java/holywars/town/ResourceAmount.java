package holywars.town;

public record ResourceAmount(long value) {

    public ResourceAmount {
        if (value < 0) {
            throw new IllegalArgumentException("Resource amount cannot be negative: " + value);
        }
    }

    public static ResourceAmount zero() {
        return new ResourceAmount(0L);
    }
}
