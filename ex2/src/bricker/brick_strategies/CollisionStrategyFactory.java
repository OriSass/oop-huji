package src.bricker.brick_strategies;

import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.bricker.main.BrickerGameManager;

import java.util.Random;

public class CollisionStrategyFactory {

    static Random random = new Random();

    BrickerGameManager brickerGameManager;
    float leftBoundary;
    float rightBoundary;
    UserInputListener inputListener;
    ImageReader imageReader;
    private final SoundReader soundReader;
    Vector2 windowDimensions;

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

    public CollisionStrategy getCollisionStrategy() {

        int randomNumber = random.nextInt(10) + 1;
        if (randomNumber > 5) {
            return new BasicCollisionStrategy(brickerGameManager);
        }
        Counter doubleStrategyCounter = new Counter();
        return getRandomSpecialStrategy(doubleStrategyCounter);
    }

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

    private CollisionStrategy getDoubleStrategy(Counter doubleStrategyCounter) {
        if(doubleStrategyCounter.value() == 3){
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
