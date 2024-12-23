package ascii_art;

/**
 * The RoundMethod enum represents the different rounding methods that can be used in the ASCII art algorithm.
 */
public enum RoundMethod {
    /**
     * Rounding method that uses the minimum value.
     */
    MIN,

    /**
     * Rounding method that uses the maximum value.
     */
    MAX,
    /**
     * Rounding method that uses the absolute value.
     */
    ABS;

    /**
     * Converts a string to a RoundMethod enum value.
     *
     * @param method the string representation of the rounding method
     * @return the corresponding RoundMethod enum value, or null if the string does not match any method
     */
    public static RoundMethod fromString(String method) {
        return switch (method) {
            case "min" -> MIN;
            case "max" -> MAX;
            case "abs" -> ABS;
            default -> null;
        };
    }
}
