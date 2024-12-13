package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.brick_strategies.CollisionStrategy;

import static bricker.utils.Constants.BRICK_TAG;

/**
 * Represents a brick in the game.
 * Handles brick-specific behavior such as collision handling.
 */
public class Brick extends GameObject {

    // The collision strategy for the brick.
    private final CollisionStrategy collisionStrategy;

    /**
     * Constructs a new Brick instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionStrategy The collision strategy to use when the brick collides with another object.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.setTag(BRICK_TAG);
    }

    /**
     * Handles the event when the brick collides with another game object.
     * Delegates the collision handling to the collision strategy.
     *
     * @param other The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        this.collisionStrategy.onCollision(this, other);
    }
}
