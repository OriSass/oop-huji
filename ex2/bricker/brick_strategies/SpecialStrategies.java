package bricker.brick_strategies;

/**
 * Enum representing special strategies in the game.
 * Each strategy is associated with a unique integer value.
 */
public enum SpecialStrategies {
    // Strategy for creating pucks.
    PUCK(1),
    // Strategy for creating extra paddles.
    EXTRA_PADDLE(2),
    // Strategy for creating turbo power-ups.
    TURBO(3),
    // Strategy for creating extra lives.
    EXTRA_LIFE(4),
    // Strategy for creating double strategies.
    DOUBLE(5);

    // The integer value associated with the strategy.
    private final int value;

    /**
     * Constructor for SpecialStrategies.
     *
     * @param value The integer value associated with the strategy.
     */
    SpecialStrategies(int value) {
        this.value = value;
    }

    /**
     * Getter for the value.
     *
     * @return The integer value associated with the strategy.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the strategy corresponding to the given value.
     *
     * @param value The integer value.
     * @return The corresponding strategy.
     * @throws IllegalArgumentException If the value does not correspond to any strategy.
     */
    public static SpecialStrategies fromValue(int value) {
        for (SpecialStrategies strategy : values()) {
            if (strategy.getValue() == value) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
