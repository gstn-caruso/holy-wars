package holywars.town;

public record ResourceAmount(long value) {

    public static ResourceAmount zero() {
        return new ResourceAmount(0L);
    }
}
