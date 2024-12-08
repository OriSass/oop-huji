package src.bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.bricker.main.BrickerGameManager;

/**
 * Represents an extra paddle in the game.
 * Handles extra paddle-specific behavior such as collision handling and removal after a certain number of hits.
 */
public class ExtraPaddle extends Paddle{

    // The maximum hit count the paddle can take before it is removed from the game
    private static final int MAX_HIT_COUNT = 4;
    // Counter for the number of extra paddles.
    private final Counter extraPaddleCounter;
    // Counter for the number of hits the paddle has taken.
    private Counter hitCounter;
    // The game manager.
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new ExtraPaddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener Allows to check what key is pressed for paddle movement.
     * @param leftBoundary  The left boundary of the game area.
     * @param rightBoundary The right boundary of the game area.
     * @param paddleCounter Counter for the number of extra paddles.
     * @param hitCounter    Counter for the number of hits the paddle has taken.
     * @param brickerGameManager The game manager.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, float leftBoundary, float rightBoundary,
                       Counter paddleCounter, Counter hitCounter, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener, leftBoundary, rightBoundary);
        this.extraPaddleCounter = paddleCounter;
        this.hitCounter = hitCounter;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the event when the extra paddle collides with another game object.
     * Increments the hit counter and removes the paddle from the game if the maximum hit count is reached.
     *
     * @param other The other game object involved in the collision.
     * @param collision The collision information.
     */
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
