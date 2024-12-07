package src.bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.bricker.main.BrickerGameManager;

public class ExtraPaddle extends Paddle{

    // The maximum hit count the paddle can take before it is removed from the game
    private static final int MAX_HIT_COUNT = 4;

    private final Counter extraPaddleCounter;
    private Counter hitCounter;
    private final BrickerGameManager brickerGameManager;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener Allows to check what key is pressed for paddle movement
     * @param leftBoundary
     * @param rightBoundary
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, float leftBoundary, float rightBoundary,
                       Counter paddleCounter, Counter hitCounter, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener, leftBoundary, rightBoundary);
        this.extraPaddleCounter = paddleCounter;
        this.hitCounter = hitCounter;
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.hitCounter.increment();

        if(this.hitCounter.value() == MAX_HIT_COUNT){
            this.hitCounter.reset();
            this.extraPaddleCounter.reset();
            this.brickerGameManager.removeGameObject(this, Layer.DEFAULT);
        }
    }
}
