package src.bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.bricker.main.BrickerGameManager;

import static src.bricker.utils.Constants.PUCK_TAG;

/**
 * Represents a puck in the game.
 * Handles puck-specific behavior such as collision handling and removal when out of bounds.
 */
public class Puck extends Ball{

    // The game manager.
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new Puck instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param collisionSound The sound to play on collision.
     * @param brickerGameManager The game manager.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.brickerGameManager = brickerGameManager;
        this.setTag(PUCK_TAG);
    }

    /**
     * Handles the event when the puck collides with another game object.
     * Delegates the collision handling to the superclass.
     *
     * @param other The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
    }

    /**
     * Updates the puck's state.
     * Removes the puck if it is out of bounds.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(this.brickerGameManager.isOutOfBounds(this.getCenter())){
            this.brickerGameManager.removeGameObject(this, Layer.DEFAULT);
        }
    }
}
