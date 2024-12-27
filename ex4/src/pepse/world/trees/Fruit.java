package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.function.BiConsumer;

import static pepse.util.Constants.AVATAR_TAG;

public class Fruit extends GameObject {
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

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(AVATAR_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.fruitHandler.accept(this, other);
    }
}
