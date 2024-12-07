package src.bricker.brick_strategies;

public enum SpecialStrategies {
    PUCK(1),
    EXTRA_PADDLE(2),
    TURBO(3),
    EXTRA_LIFE(4),
    DOUBLE(5);

    private final int value;

    // Constructor
    SpecialStrategies(int value) {
        this.value = value;
    }

    // Getter for the value
    public int getValue() {
        return value;
    }

    public static SpecialStrategies fromValue(int value) {
        for (SpecialStrategies strategy : values()) {
            if (strategy.getValue() == value) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
