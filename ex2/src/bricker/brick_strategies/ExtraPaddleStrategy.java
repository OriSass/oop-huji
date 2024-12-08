package src.bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.bricker.gameobjects.ExtraPaddle;
import src.bricker.main.BrickerGameManager;

import static src.bricker.utils.Constants.*;

public class ExtraPaddleStrategy extends BasicCollisionStrategy {

    public static final Counter extraPaddleCounter = new Counter();
    public static final Counter hitCounter = new Counter();

    private final BrickerGameManager brickerGameManager;
    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private final float leftBoundary;
    private final float rightBoundary;
    private final Vector2 windowDimensions;

    public ExtraPaddleStrategy(BrickerGameManager brickerGameManager,
                               UserInputListener inputListener,
                               ImageReader imageReader, float leftBoundary,
                               float rightBoundary, Vector2 windowDimensions) {
        super(brickerGameManager);
        this.brickerGameManager = brickerGameManager;
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.windowDimensions = windowDimensions;
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        // remove brick
        super.onCollision(gameObject1, gameObject2);
        addExtraPaddle();
    }

    public void addExtraPaddle() {
        if(extraPaddleCounter.value() > 0){
            return;
        }

        // if we got here we need to create an extra paddle
        extraPaddleCounter.increment();
        Renderable paddleImage = this.imageReader.readImage(PADDLE_IMAGE_PATH, true);
        GameObject extraPaddle = new ExtraPaddle(Vector2.ZERO, PADDLE_DIMENSIONS,
                paddleImage, this.inputListener, this.leftBoundary, this.rightBoundary,
                extraPaddleCounter, hitCounter, this.brickerGameManager);
        extraPaddle.setCenter(this.windowDimensions.mult(HALF));
        this.brickerGameManager.addGameObject(extraPaddle, Layer.DEFAULT);
    }
}
