package src.bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import static src.bricker.utils.Constants.TURBO_SPEED_FACTOR;

/**
 * Represents a ball in the game.
 * Handles ball-specific behavior such as collision handling and turbo mode.
 */
public class Ball extends GameObject {

    // The default image of the ball.
    private final Renderable ballImage;
    // The sound to play when a collision occurs.
    private final Sound collisionSound;
    // Whether the ball is in turbo mode.
    private boolean turboOn;
    // Counter for the number of collisions.
    private final Counter collisionCounter;



    /**
     * Constructs a new Ball instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param ballImage     The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionSound The sound to play on collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable ballImage,
                Sound collisionSound) {
        super(topLeftCorner, dimensions, ballImage);
        this.ballImage = ballImage;
        this.collisionSound = collisionSound;
        this.collisionCounter = new Counter();
        this.turboOn = false;
    }

    /**
     * Handles the event when the ball collides with another game object.
     * Flips the ball's velocity based on the collision normal and plays the collision sound.
     *
     * @param other The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
        collisionCounter.increment();
        collisionSound.play();
        handleTurbo();
    }

    /**
     * Handles the turbo mode logic.
     * If turbo mode is enabled and the collision counter reaches a threshold, resets the ball to normal mode.
     */
    private void handleTurbo() {
        if (!turboOn) {
            return;
        }
        // if we got here we are in turbo mode
        if(getCollisionCounter() >= 6){
            resetBallToNormal();
        }
    }

    /**
     * Gets the current collision counter value.
     *
     * @return The number of collisions.
     */
    public int getCollisionCounter() {
        return collisionCounter.value();
    }

    /**
     * Resets the collision counter to zero.
     */
    public void resetCollisionCounter() {
        collisionCounter.reset();
    }

    /**
     * Gets the collision sound.
     *
     * @return The collision sound.
     */
    public Sound getCollisionSound() {
        return collisionSound;
    }

    /**
     * Checks if turbo mode is enabled.
     *
     * @return True if turbo mode is enabled, false otherwise.
     */
    public boolean isTurboOn() {
        return turboOn;
    }

    /**
     * Sets the turbo mode state.
     *
     * @param turboOn True to enable turbo mode, false to disable it.
     */
    public void setTurboOn(boolean turboOn) {
        this.turboOn = turboOn;
    }

    /**
     * Resets the ball to its normal state.
     * Disables turbo mode, resets the ball's image, and adjusts its velocity.
     */
    public void resetBallToNormal() {
        this.turboOn = false;
        this.renderer().setRenderable(this.ballImage);
        Vector2 normalVelocity = getVelocity().mult(1f / TURBO_SPEED_FACTOR);
        this.setVelocity(normalVelocity);
    }
}
