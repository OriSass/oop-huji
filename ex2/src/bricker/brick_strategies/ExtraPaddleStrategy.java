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

/**
 * A collision strategy that adds an extra paddle to the game.
 * When a collision occurs, this strategy creates an additional paddle.
 */
public class ExtraPaddleStrategy extends BasicCollisionStrategy {

    // Counter for the number of extra paddles.
    public static final Counter extraPaddleCounter = new Counter();
    // Counter for the number of hits.
    public static final Counter hitCounter = new Counter();

    // The game manager.
    private final BrickerGameManager brickerGameManager;
    // Listens for user input.
    private final UserInputListener inputListener;
    // Reads images for rendering.
    private final ImageReader imageReader;
    // The left boundary of the game area.
    private final float leftBoundary;
    // The right boundary of the game area.
    private final float rightBoundary;
    // The dimensions of the game window.
    private final Vector2 windowDimensions;

    /**
     * Constructs an ExtraPaddleStrategy.
     *
     * @param brickerGameManager The game manager.
     * @param inputListener      Listens for user input.
     * @param imageReader        Reads images for rendering.
     * @param leftBoundary       The left boundary of the game area.
     * @param rightBoundary      The right boundary of the game area.
     * @param windowDimensions   The dimensions of the game window.
     */
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

    /**
     * Handles the collision between two game objects.
     * Creates an additional paddle when a collision occurs.
     *
     * @param gameObject1 The first game object.
     * @param gameObject2 The second game object.
     */
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        // remove brick
        super.onCollision(gameObject1, gameObject2);
        addExtraPaddle();
    }

    /**
     * Adds an extra paddle to the game.
     * This method creates an extra paddle if the player has not already received one.
     */
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
