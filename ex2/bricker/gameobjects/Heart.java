package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.main.BrickerGameManager;

import static bricker.utils.Constants.*;

/**
 * Represents a heart in the game.
 * Handles heart-specific behavior such as collision handling and removal when collected.
 */
public class Heart extends GameObject {

    // The game manager.
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new Heart instance.
     *
     * @param heartPosition Position of the heart, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param heartImage    The renderable representing the heart. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param brickerGameManager The game manager.
     */
    public Heart(Vector2 heartPosition, Renderable heartImage, BrickerGameManager brickerGameManager) {
        super(heartPosition, HEART_DIMENSION, heartImage);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Determines if the heart should collide with another game object.
     *
     * @param other The other game object.
     * @return True if the other game object is a paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(PADDLE_TAG);
    }

    /**
     * Handles the event when the heart collides with another game object.
     * Removes the heart and updates the life count if it is below the maximum.
     *
     * @param other The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(this.brickerGameManager.getLifeCount() < MAX_LIFE_COUNT){
            this.brickerGameManager.removeGameObject(this, Layer.DEFAULT);
            this.brickerGameManager.updateLives();
        }
    }

    /**
     * Updates the heart's state.
     * Removes the heart if it is out of bounds.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(!this.getVelocity().isZero()){
            if(this.brickerGameManager.isOutOfBounds(this.getCenter())){
                this.brickerGameManager.removeGameObject(this, Layer.DEFAULT);
            }
        }
    }
}
