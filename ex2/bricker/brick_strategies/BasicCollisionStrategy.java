package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import bricker.main.BrickerGameManager;

/**
 * Basic collision strategy for bricks.
 * This strategy removes the brick from the game when a collision occurs.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    // The game manager.
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a BasicCollisionStrategy.
     *
     * @param brickerGameManager The game manager.
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between two game objects.
     * Removes the first game object (assumed to be a brick) from the game.
     *
     * @param gameObject1 The first game object.
     * @param gameObject2 The second game object.
     */
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        this.brickerGameManager.removeGameObject(gameObject1, Layer.STATIC_OBJECTS);
    }
}
