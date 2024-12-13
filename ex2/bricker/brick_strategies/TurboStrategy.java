package bricker.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;

import static bricker.utils.Constants.*;

/**
 * A collision strategy that enables turbo mode for the ball.
 * When a collision occurs, this strategy sets the ball to turbo mode.
 */
public class TurboStrategy extends BasicCollisionStrategy {

    // Reads images for rendering.
    private final ImageReader imageReader;

    /**
     * Constructs a TurboStrategy.
     *
     * @param brickerGameManager The game manager.
     * @param imageReader        Reads images for rendering.
     */
    public TurboStrategy(BrickerGameManager brickerGameManager, ImageReader imageReader) {
        super(brickerGameManager);
        this.imageReader = imageReader;
    }

    /**
     * Handles the collision between two game objects.
     * Sets the ball to turbo mode when a collision occurs.
     *
     * @param gameObject1 The first game object (brick).
     * @param gameObject2 The second game object (ball or puck).
     */
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        handleTurbo(gameObject2);
    }

    /**
     * Enables turbo mode for the ball.
     * This method sets the ball to turbo mode, increasing its speed and changing its appearance.
     *
     * @param gameObject2 The game object that caused the collision (ball or puck).
     */
    private void handleTurbo(GameObject gameObject2) {
        if (gameObject2.getTag().equals(PUCK_TAG)){
            return;
        }
        // if we got here the collision was caused by the "main" ball

        // check not in turbo mode
        Ball ball = (Ball) gameObject2;
        if (ball.isTurboOn()){
            return;
        }
        // if we got here we need to turn on turbo mode
        Renderable turboBallImage = this.imageReader.readImage(
                TURBO_BALL_IMAGE_PATH, true);
        ball.renderer().setRenderable(turboBallImage);
        ball.setVelocity(ball.getVelocity().mult(TURBO_SPEED_FACTOR));
        ball.setTurboOn(true);
        ball.resetCollisionCounter();
    }
}
