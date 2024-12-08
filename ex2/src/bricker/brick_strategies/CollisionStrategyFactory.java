package src.bricker.brick_strategies;

import danogl.gui.ImageReader;
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
    Vector2 windowDimensions;

    public CollisionStrategyFactory(BrickerGameManager brickerGameManager, float leftBoundary,
                                    float rightBoundary, UserInputListener inputListener,
                                    ImageReader imageReader, Vector2 windowDimensions) {
        this.brickerGameManager = brickerGameManager;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.inputListener = inputListener;
        this.imageReader = imageReader;
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
        CollisionStrategy strategy;
        switch (strategyType) {
            case SpecialStrategies.PUCK:
                strategy = new PuckStrategy(brickerGameManager);
                break;

            case SpecialStrategies.EXTRA_PADDLE:
                strategy = new ExtraPaddleStrategy(brickerGameManager, inputListener,
                    imageReader, leftBoundary, rightBoundary, windowDimensions);
                break;

            case SpecialStrategies.TURBO:
                strategy = new TurboStrategy(brickerGameManager, imageReader);
                break;
            case SpecialStrategies.EXTRA_LIFE:
                strategy = new ExtraLifeStrategy(brickerGameManager, imageReader);
                break;

            default:
                if(doubleStrategyCounter.value() == 3){
                    strategy = null;
                    break;
                }
                doubleStrategyCounter.increment();
                CollisionStrategy strategy1 = getRandomSpecialStrategy(doubleStrategyCounter);
                CollisionStrategy strategy2 = getRandomSpecialStrategy(doubleStrategyCounter);
                if(strategy1 == null && strategy2 != null){
                    strategy = strategy2;
                    break;
                }
                else if(strategy2 == null && strategy1 != null){
                    strategy = strategy1;
                    break;
                }
                else{
                    strategy = new DoubleStrategy(brickerGameManager, strategy1, strategy2);
                    break;
                }

        }
        return strategy;
    }
}
