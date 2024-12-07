package src.bricker.brick_strategies;

import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;
import src.bricker.main.BrickerGameManager;

import java.util.Random;

public class CollisionStrategyFactory {

    static Random random = new Random();

    public CollisionStrategy getCollisionStrategy(
            BrickerGameManager brickerGameManager,
            float leftBoundary, float rightBoundary,
            UserInputListener inputListener,
            ImageReader imageReader, Vector2 windowDimensions) {

//        int randomNumber = random.nextInt(10) + 1;
        int randomNumber = 4;
        if (randomNumber > 5) {
            return new BasicCollisionStrategy(brickerGameManager);
        }
        SpecialStrategies strategy = SpecialStrategies.fromValue(randomNumber);
        return switch (strategy) {
            case SpecialStrategies.PUCK -> new PuckStrategy(brickerGameManager);
            case SpecialStrategies.EXTRA_PADDLE -> new ExtraPaddleStrategy(brickerGameManager, inputListener,
                    imageReader, leftBoundary, rightBoundary, windowDimensions);
            case SpecialStrategies.TURBO -> new TurboStrategy(brickerGameManager, imageReader);
            case SpecialStrategies.EXTRA_LIFE -> new ExtraLifeStrategy(brickerGameManager, imageReader);
            default -> null;
//            case SpecialStrategies.DOUBLE:
//                break;
//            default:
//                break;
        };
    }
}
