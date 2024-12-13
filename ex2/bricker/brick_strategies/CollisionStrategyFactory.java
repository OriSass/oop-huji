package bricker.brick_strategies;

import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.main.BrickerGameManager;
import java.util.Random;

import static bricker.utils.Constants.DOUBLE_STRATEGY_LIMIT;

/**
 * Factory for creating collision strategies for bricks.
 * This factory provides methods to create different types of collision strategies
 * based on the game state and other parameters.
 */
public class CollisionStrategyFactory {

    // Random number generator for selecting strategies.
    static Random random = new Random();

    // The game manager.
    BrickerGameManager brickerGameManager;
    // The left boundary of the game area.
    float leftBoundary;
    // The right boundary of the game area.
    float rightBoundary;
    // Listens for user input.
    UserInputListener inputListener;
    // Reads images for rendering.
    ImageReader imageReader;
    // Reads sounds for playback.
    private final SoundReader soundReader;
    // The dimensions of the game window.
    Vector2 windowDimensions;

    /**
     * Constructs a CollisionStrategyFactory.
     *
     * @param brickerGameManager The game manager.
     * @param leftBoundary       The left boundary of the game area.
     * @param rightBoundary      The right boundary of the game area.
     * @param inputListener      Listens for user input.
     * @param imageReader        Reads images for rendering.
     * @param soundReader        Reads sounds for playback.
     * @param windowDimensions   The dimensions of the game window.
     */
    public CollisionStrategyFactory(BrickerGameManager brickerGameManager, float leftBoundary,
                                    float rightBoundary, UserInputListener inputListener,
                                    ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions) {
        this.brickerGameManager = brickerGameManager;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Retrieves a collision strategy for a brick.
     * This method returns either a basic collision strategy or a random special strategy.
     *
     * @return A CollisionStrategy instance.
     */
    public CollisionStrategy getCollisionStrategy() {

        int randomNumber = random.nextInt(10) + 1;
        if (randomNumber > 5) {
            return new BasicCollisionStrategy(brickerGameManager);
        }
        Counter doubleStrategyCounter = new Counter();
        return getRandomSpecialStrategy(doubleStrategyCounter);
    }

    /**
     * Retrieves a random special collision strategy.
     * This method returns a special collision strategy based on a random selection.
     *
     * @param doubleStrategyCounter A counter for double strategies.
     * @return A CollisionStrategy instance.
     */
    private CollisionStrategy getRandomSpecialStrategy(Counter doubleStrategyCounter) {
        int randomNumber = random.nextInt(5) + 1;
        SpecialStrategies strategyType = SpecialStrategies.fromValue(randomNumber);
        return switch (strategyType) {
            case SpecialStrategies.PUCK ->
                    new PuckStrategy(this.brickerGameManager, this.imageReader, this.soundReader);
            case SpecialStrategies.EXTRA_PADDLE ->
                    new ExtraPaddleStrategy(this.brickerGameManager, this.inputListener,
                            this.imageReader, this.leftBoundary, this.rightBoundary, windowDimensions);
            case SpecialStrategies.TURBO -> new TurboStrategy(brickerGameManager, imageReader);
            case SpecialStrategies.EXTRA_LIFE -> new ExtraLifeStrategy(brickerGameManager, imageReader);
            default -> getDoubleStrategy(doubleStrategyCounter);
        };
    }

    /**
     * Retrieves a double collision strategy.
     * This method returns a collision strategy that applies two different strategies.
     *
     * @param doubleStrategyCounter A counter for double strategies.
     * @return A CollisionStrategy instance.
     */
    private CollisionStrategy getDoubleStrategy(Counter doubleStrategyCounter) {
        if(doubleStrategyCounter.value() == DOUBLE_STRATEGY_LIMIT){
            return null;
        }
        doubleStrategyCounter.increment();
        CollisionStrategy strategy1 = getRandomSpecialStrategy(doubleStrategyCounter);
        CollisionStrategy strategy2 = getRandomSpecialStrategy(doubleStrategyCounter);
        if(strategy1 == null && strategy2 != null){
            return strategy2;
        }
        else if(strategy2 == null && strategy1 != null){
            return strategy1;
        }
        else{
            return new DoubleStrategy(brickerGameManager, strategy1, strategy2);
        }
    }
}
