package src.bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import static src.bricker.utils.Constants.TURBO_SPEED_FACTOR;

public class Ball extends GameObject {

    private final Renderable ballImage;
    private final Sound collisionSound;
    private boolean turboOn;

    private final Counter collisionCounter;



    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param ballImage    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable ballImage,
                Sound collisionSound) {
        super(topLeftCorner, dimensions, ballImage);
        this.ballImage = ballImage;
        this.collisionSound = collisionSound;
        this.collisionCounter = new Counter();
        this.turboOn = false;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
        collisionCounter.increment();
        collisionSound.play();
        handleTurbo();
    }

    private void handleTurbo() {
        if (!turboOn) {
            return;
        }
        // if we got here we are in turbo mode
        if(getCollisionCounter() >= 6){
            this.turboOn = false;
            this.renderer().setRenderable(this.ballImage);
            Vector2 normalVelocity = getVelocity().mult(1f / TURBO_SPEED_FACTOR);
            this.setVelocity(normalVelocity);
        }
    }


    public int getCollisionCounter() {
        return collisionCounter.value();
    }
    public void resetCollisionCounter() {
        collisionCounter.reset();
    }

    public Sound getCollisionSound() {
        return collisionSound;
    }

    public boolean isTurboOn() {
        return turboOn;
    }

    public void setTurboOn(boolean turboOn) {
        this.turboOn = turboOn;
    }

}
