package ascii_art;

public enum RoundMethod {
    MIN,
    MAX,
    ABS;

    public static RoundMethod fromString(String method) {
        return switch (method) {
            case "min" -> MIN;
            case "max" -> MAX;
            case "abs" -> ABS;
            default -> null;
        };
    }
}
