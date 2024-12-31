package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.function.BiConsumer;

import static pepse.util.Constants.AVATAR_TAG;

/**
 * A class representing a fruit in the game.
 */
public class Fruit extends GameObject {
    /**
     * Callback passed from the game manager, handling the avatar eating the fruit.
     */
    private final BiConsumer<Fruit, GameObject> fruitHandler;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param fruitHandler  callback passed from the game manager, handling the avatar eating fruit
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 BiConsumer<Fruit, GameObject> fruitHandler) {
        super(topLeftCorner, dimensions, renderable);
        this.fruitHandler = fruitHandler;
    }

    /**
     * Determines if this fruit should collide with another game object.
     *
     * @param other The other game object.
     * @return True if the other game object has the avatar tag, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(AVATAR_TAG);
    }

    /**
     * Handles the collision event when this fruit collides with another game object.
     *
     * @param other     The other game object.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.fruitHandler.accept(this, other);
    }
}
