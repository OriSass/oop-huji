package src.bricker.brick_strategies;

import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;
import src.bricker.main.BrickerGameManager;

import java.util.Random;

public class CollisionStrategyFactory {

    static Random random = new Random();

    public CollisionStrategy getCollisionStrategy(
            BrickerGameManager brickerGameManager,
            float leftBoundary, float rightBoundary,
            UserInputListener inputListener,
            ImageRenderable paddleImage, Vector2 windowDimensions) {

//        int randomNumber = random.nextInt(10) + 1;
        int randomNumber = 2;
        if (randomNumber > 5) {
            return new BasicCollisionStrategy(brickerGameManager);
        }
        SpecialStrategies strategy = SpecialStrategies.fromValue(randomNumber);
        switch (strategy) {
            case SpecialStrategies.PUCK:
                return new PuckStrategy(brickerGameManager);
            case SpecialStrategies.EXTRA_PADDLE:
                return new ExtraPaddleStrategy(brickerGameManager, inputListener,
                        paddleImage, leftBoundary, rightBoundary, windowDimensions);
            case SpecialStrategies.TURBO:
                return new TurboStrategy(brickerGameManager);
            default:
                return null;
//            case SpecialStrategies.EXTRA_LIFE:
//                break;
//            case SpecialStrategies.DOUBLE:
//                break;
//            default:
//                break;
        }
    }
}
